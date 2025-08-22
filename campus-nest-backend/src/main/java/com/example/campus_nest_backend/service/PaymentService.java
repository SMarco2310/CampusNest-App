package com.example.campus_nest_backend.service;

import com.example.campus_nest_backend.entity.*;
import com.example.campus_nest_backend.repository.BookingRepository;
import com.example.campus_nest_backend.repository.PaymentRepository;
import com.example.campus_nest_backend.utils.Status;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import okhttp3.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class PaymentService {

    @Value("${paystack.secret}")
    private String PAYSTACK_SECRET;

    private final ObjectMapper mapper = new ObjectMapper();
    private final OkHttpClient client = new OkHttpClient();
    private final PaymentRepository paymentRepository;
    private final BookingRepository bookingRepository;

    // 1️⃣ Initialize Payment
    public Map<String, Object> initializePayment(String email, Integer amount, Long bookingId) throws IOException {
        Map<String, Object> metadata = new HashMap<>();
        metadata.put("bookingId", bookingId);

        Map<String, Object> payload = new HashMap<>();
        payload.put("email", email);
        payload.put("amount", amount);
        payload.put("currency", "GHS");
        payload.put("channels", new String[]{"card", "bank_transfer"});
        payload.put("metadata", metadata);

        String json = mapper.writeValueAsString(payload);
        RequestBody body = RequestBody.create(json, MediaType.parse("application/json"));

        Request paystackRequest = new Request.Builder()
                .url("https://api.paystack.co/transaction/initialize")
                .post(body)
                .addHeader("Authorization", "Bearer " + PAYSTACK_SECRET)
                .addHeader("Content-Type", "application/json")
                .build();

        try (Response response = client.newCall(paystackRequest).execute()) {
            if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);
            return mapper.readValue(response.body().string(), Map.class);
        }
    }

    // 2️⃣ Verify Payment & Update Booking + Create Payment
    public Payment verifyAndProcessPayment(String reference) throws Exception {
        Request paystackRequest = new Request.Builder()
                .url("https://api.paystack.co/transaction/verify/" + reference)
                .get()
                .addHeader("Authorization", "Bearer " + PAYSTACK_SECRET)
                .build();

        Map<String, Object> result;
        try (Response response = client.newCall(paystackRequest).execute()) {
            if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);
            result = mapper.readValue(response.body().string(), Map.class);
        }

        Map<String, Object> data = (Map<String, Object>) result.get("data");
        String status = (String) data.get("status");

        if (!"success".equals(status)) {
            throw new IllegalStateException("Payment not successful");
        }

        // ✅ Get bookingId from metadata
        Map<String, Object> metadata = (Map<String, Object>) data.get("metadata");
        Long bookingId = Long.valueOf(metadata.get("bookingId").toString());

        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new IllegalStateException("Booking not found for id: " + bookingId));

        BigDecimal amount = BigDecimal.valueOf(((Number) data.get("amount")).intValue()).divide(BigDecimal.valueOf(100));

        // 1️⃣ Save Payment
        Payment payment = new Payment();
        payment.setPaystackReference(reference);
        payment.setBooking(booking);
        payment.setAmount(amount);
        payment.setStatus(Payment.Status.PAID);
        payment.setCurrency("GHS");
        payment.setPaidAt(OffsetDateTime.now());

        paymentRepository.save(payment);

        // 2️⃣ Update Booking Status
        booking.setStatus(Status.CONFIRMED);
        bookingRepository.save(booking);

        // 3️⃣ Pay Hostel Manager
        Hostel hostel = booking.getRoom().getHostel();
        Hostel_Manager manager = hostel.getManager();

        // Need to find a way in the system to get the bank account details of the hostel manager based on the index
        BankAccountDetails account = manager.getBankAccountDetails().get(booking.getPaymentModeIndex()); // take first account

        payHostelManager(account.getAccountName(),
                account.getBankCode(),
                account.getAccountNumber(),
                amount.multiply(BigDecimal.valueOf(100)).intValue()); // amount in pesewas

        return payment;
    }

    private void payHostelManager(String name, String bankCode, String accountNumber, int amount) throws Exception {
        Map<String, Object> recipientPayload = new HashMap<>();
        recipientPayload.put("type", "nuban");
        recipientPayload.put("name", name);
        recipientPayload.put("account_number", accountNumber);
        recipientPayload.put("bank_code", bankCode);
        recipientPayload.put("currency", "GHS");

        String recipientJson = mapper.writeValueAsString(recipientPayload);
        RequestBody recipientBody = RequestBody.create(recipientJson, MediaType.parse("application/json"));
        Request createRecipient = new Request.Builder()
                .url("https://api.paystack.co/transferrecipient")
                .post(recipientBody)
                .addHeader("Authorization", "Bearer " + PAYSTACK_SECRET)
                .addHeader("Content-Type", "application/json")
                .build();

        String recipientCode;
        try (Response response = client.newCall(createRecipient).execute()) {
            Map<String, Object> res = mapper.readValue(response.body().string(), Map.class);
            Map<String, Object> data = (Map<String, Object>) res.get("data");
            recipientCode = (String) data.get("recipient_code");
        }

        Map<String, Object> transferPayload = new HashMap<>();
        transferPayload.put("source", "balance");
        transferPayload.put("amount", amount);
        transferPayload.put("recipient", recipientCode);
        transferPayload.put("reason", "Hostel Booking Payment");

        String transferJson = mapper.writeValueAsString(transferPayload);
        RequestBody transferBody = RequestBody.create(transferJson, MediaType.parse("application/json"));
        Request transferRequest = new Request.Builder()
                .url("https://api.paystack.co/transfer")
                .post(transferBody)
                .addHeader("Authorization", "Bearer " + PAYSTACK_SECRET)
                .addHeader("Content-Type", "application/json")
                .build();

        try (Response response = client.newCall(transferRequest).execute()) {
            System.out.println("Payout Response: " + response.body().string());
        }
    }

    public String handleWebhook(Map<String, Object> payload, String signature) throws Exception {
        String jsonPayload = mapper.writeValueAsString(payload);
        String computedHash = computeHmacSHA512(jsonPayload, PAYSTACK_SECRET);

        if (!computedHash.equals(signature)) return "Invalid signature";

        String event = (String) payload.get("event");
        if ("charge.success".equals(event)) {
            Map<String, Object> data = (Map<String, Object>) payload.get("data");
            String reference = (String) data.get("reference");
            verifyAndProcessPayment(reference);
        }
        return "OK";
    }

    private String computeHmacSHA512(String data, String key) throws Exception {
        Mac sha512_HMAC = Mac.getInstance("HmacSHA512");
        SecretKeySpec secret_key = new SecretKeySpec(key.getBytes(), "HmacSHA512");
        sha512_HMAC.init(secret_key);

        byte[] hash = sha512_HMAC.doFinal(data.getBytes());
        StringBuilder sb = new StringBuilder();
        for (byte b : hash) sb.append(String.format("%02x", b));
        return sb.toString();
    }
}
