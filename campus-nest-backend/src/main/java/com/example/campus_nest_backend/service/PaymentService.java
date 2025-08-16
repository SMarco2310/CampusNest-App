//package com.example.campus_nest_backend.service;
//
//import com.example.campus_nest_backend.entity.*;
//import com.example.campus_nest_backend.repository.BookingRepository;
//import com.example.campus_nest_backend.repository.PaymentRepository;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import lombok.RequiredArgsConstructor;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.http.*;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//import org.springframework.web.client.RestTemplate;
//
//import javax.crypto.Mac;
//import javax.crypto.spec.SecretKeySpec;
//import java.math.BigDecimal;
//import java.nio.charset.StandardCharsets;
//import java.time.OffsetDateTime;
//import java.util.*;
//
//@Service
//@RequiredArgsConstructor
//public class PaymentService {
//
//    private final RestTemplate restTemplate;
//    private final BookingRepository bookingRepository;
//    private final PaymentRepository paymentRepository;
//    private final ObjectMapper objectMapper = new ObjectMapper();
//
//    @Value("${paystack.secret.key}")
//    private String paystackSecretKey;
//    @Value("${paystack.base.url}")
//    private String paystackBaseUrl;
//
//    // ---------- 1) Initialize a payment for a booking ----------
//    @Transactional
//    public Map<String, Object> initPayment(Long bookingId) {
//        Booking booking = bookingRepository.findById(bookingId)
//                .orElseThrow(() -> new RuntimeException("Booking not found"));
//
//        // Create a unique reference you control
//        String reference = "BK-" + booking.getId() + "-" + UUID.randomUUID();
//
//        // Create a Payment row (PENDING)
//        Payment payment = new Payment();
//        payment.setBooking(booking);
//        payment.setAmount(BigDecimal.valueOf(booking.getAmount())); // booking.getAmount() should be major units
//        payment.setPaystackReference(reference);
//        paymentRepository.save(payment);
//
//        // Build request to Paystack initialize
//        HttpHeaders headers = authHeaders();
//        Map<String, Object> body = new HashMap<>();
//        body.put("email", booking.getUser().getEmail());
//        body.put("amount", kobo(booking.getAmount())); // Paystack expects minor units
//        // put bookingId in metadata so webhook can find it
//        Map<String, Object> metadata = new HashMap<>();
//        metadata.put("bookingId", booking.getId());
//        metadata.put("reference", reference);
//        body.put("metadata", metadata);
//
//        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(body, headers);
//
//        ResponseEntity<Map> response = restTemplate.postForEntity(
//                paystackBaseUrl + "/transaction/initialize", entity, Map.class);
//
//        if (!response.getStatusCode().is2xxSuccessful() || response.getBody() == null) {
//            throw new RuntimeException("Payment initialization failed");
//        }
//
//        Map data = (Map) response.getBody().get("data");
//        Map<String, Object> result = new HashMap<>();
//        result.put("authorizationUrl", data.get("authorization_url"));
//        result.put("reference", reference);
//        return result;
//    }
//
//    // ---------- 2) Webhook verification ----------
//    public boolean verifySignature(String requestBody, String paystackSignatureHeader) {
//        try {
//            Mac sha512Hmac = Mac.getInstance("HmacSHA512");
//            SecretKeySpec keySpec = new SecretKeySpec(paystackSecretKey.getBytes(StandardCharsets.UTF_8), "HmacSHA512");
//            sha512Hmac.init(keySpec);
//            byte[] macData = sha512Hmac.doFinal(requestBody.getBytes(StandardCharsets.UTF_8));
//            String expected = bytesToHex(macData);
//            return expected.equalsIgnoreCase(paystackSignatureHeader);
//        } catch (Exception e) {
//            return false;
//        }
//    }
//
//    private String bytesToHex(byte[] bytes) {
//        StringBuilder out = new StringBuilder(bytes.length * 2);
//        for (byte b : bytes) out.append(String.format("%02x", b));
//        return out.toString();
//    }
//
//    // ---------- 3) Handle webhook (idempotent) ----------
//    @Transactional
//    public void handleWebhook(String rawBody) {
//        try {
//            Map payload = objectMapper.readValue(rawBody, Map.class);
//            Map data = (Map) payload.get("data");
//            String status = (String) data.get("status");
//            Map md = (Map) data.get("metadata");
//            String reference = (String) md.getOrDefault("reference", data.get("reference"));
//            Long bookingId = ((Number) md.get("bookingId")).longValue();
//
//            Payment payment = paymentRepository.findByPaystackReference(reference)
//                    .orElseThrow(() -> new RuntimeException("Payment not found by reference"));
//            Booking booking = bookingRepository.findById(bookingId)
//                    .orElseThrow(() -> new RuntimeException("Booking not found"));
//
//            if (!"success".equalsIgnoreCase(status)) {
//                payment.setStatus(Payment.Status.FAILED);
//                paymentRepository.save(payment);
//                return;
//            }
//
//            // If already paid out, ignore (idempotency)
//            if (payment.getStatus() == Payment.Status.PAID_OUT) return;
//
//            // Mark booking paid if not already
//            booking.setStatus("PAID");
//            bookingRepository.save(booking);
//
//            // Mark payment PAID and timestamp
//            if (payment.getStatus() == Payment.Status.PENDING) {
//                payment.setStatus(Payment.Status.PAID);
//                payment.setPaidAt(OffsetDateTime.now());
//                paymentRepository.save(payment);
//            }
//
//            // Immediately payout full amount
//            if (payment.getStatus() == Payment.Status.PAID) {
//                payoutToHostelManager(payment);
//            }
//
//        } catch (Exception e) {
//            throw new RuntimeException("Webhook handling error: " + e.getMessage(), e);
//        }
//    }
//
//    // ---------- 4) Create transfer recipient and transfer full amount ----------
//    @Transactional
//    public void payoutToHostelManager(Payment payment) {
//        Booking booking = payment.getBooking();
//        Hostel hostel = booking.getHostel();
//        HostelManager manager = hostel.getManager();
//
//        HttpHeaders headers = authHeaders();
//
//        // A) Create/Use transfer recipient (BANK)
//        Map<String, Object> recipientReq = new HashMap<>();
//        /*
//          For GHANA BANK: type "nuban" applies to NG; Paystack for Ghana typically uses "ghipss" or bank code mapping.
//          Many teams still use: type: "nuban" for bank accounts in NG.
//          For Ghana, you can instead do Mobile Money (below).
//          If you have bank payouts configured in Paystack Ghana, set type/bank_code per your Paystack dashboard requirements.
//        */
//        recipientReq.put("type", "nuban"); // adjust per your region config
//        recipientReq.put("name", manager.getName());
//        recipientReq.put("account_number", manager.getBankAccountNumber());
//        recipientReq.put("bank_code", manager.getBankCode());  // you must store this on HostelManager
//        recipientReq.put("currency", "GHS");
//
//        HttpEntity<Map<String, Object>> recEntity = new HttpEntity<>(recipientReq, headers);
//        ResponseEntity<Map> recRes = restTemplate.postForEntity(
//                paystackBaseUrl + "/transferrecipient", recEntity, Map.class);
//
//        if (!recRes.getStatusCode().is2xxSuccessful() || recRes.getBody() == null) {
//            throw new RuntimeException("Failed to create transfer recipient");
//        }
//        Map recData = (Map) recRes.getBody().get("data");
//        String recipientCode = (String) recData.get("recipient_code");
//        payment.setTransferRecipientCode(recipientCode);
//        paymentRepository.save(payment);
//
//        // B) Initiate transfer (full amount)
//        Map<String, Object> transferReq = new HashMap<>();
//        transferReq.put("source", "balance");
//        transferReq.put("amount", kobo(payment.getAmount().doubleValue())); // in minor units
//        transferReq.put("recipient", recipientCode);
//        transferReq.put("reason", "Hostel booking payment - Booking #" + booking.getId());
//
//        HttpEntity<Map<String, Object>> trEntity = new HttpEntity<>(transferReq, headers);
//        ResponseEntity<Map> trRes = restTemplate.postForEntity(
//                paystackBaseUrl + "/transfer", trEntity, Map.class);
//
//        if (!trRes.getStatusCode().is2xxSuccessful() || trRes.getBody() == null) {
//            throw new RuntimeException("Failed to create transfer");
//        }
//        Map trData = (Map) trRes.getBody().get("data");
//        payment.setTransferCode((String) trData.get("transfer_code"));
//        payment.setStatus(Payment.Status.PAID_OUT);
//        payment.setPaidOutAt(OffsetDateTime.now());
//        paymentRepository.save(payment);
//    }
//
//    // ---------- Optional: switch to Mobile Money payout ----------
//    public void payoutMoMo(Hostel_Manager manager, double amountMajor) {
//        HttpHeaders headers = authHeaders();
//        Map<String, Object> recipientReq = new HashMap<>();
//        recipientReq.put("type", "mobile_money");
//        recipientReq.put("name", manager.getName());
//        recipientReq.put("currency", "GHS");
//        Map<String, Object> details = new HashMap<>();
//        details.put("phone", manager.getMomoNumber());
//        details.put("provider", manager.getMomoProvider()); // "mtn", "vodafone", "airtel_tigo"
//        recipientReq.put("details", details);
//
//        HttpEntity<Map<String, Object>> recEntity = new HttpEntity<>(recipientReq, headers);
//        Map rec = restTemplate.postForObject(paystackBaseUrl + "/transferrecipient", recEntity, Map.class);
//        String recipientCode = (String) ((Map) rec.get("data")).get("recipient_code");
//
//        Map<String, Object> transferReq = new HashMap<>();
//        transferReq.put("source", "balance");
//        transferReq.put("amount", kobo(amountMajor));
//        transferReq.put("recipient", recipientCode);
//        transferReq.put("reason", "Hostel booking payment (MoMo)");
//
//        restTemplate.postForEntity(paystackBaseUrl + "/transfer", new HttpEntity<>(transferReq, headers), Map.class);
//    }
//
//    private HttpHeaders authHeaders() {
//        HttpHeaders headers = new HttpHeaders();
//        headers.setBearerAuth(paystackSecretKey);
//        headers.setContentType(MediaType.APPLICATION_JSON);
//        return headers;
//    }
//
//    private long kobo(double major) {
//        // GHS 1.00 -> 100 (minor units)
//        return Math.round(major * 100);
//    }
//}