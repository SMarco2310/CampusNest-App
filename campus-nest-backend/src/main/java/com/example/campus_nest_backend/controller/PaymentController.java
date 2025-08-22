package com.example.campus_nest_backend.controller;

import com.example.campus_nest_backend.entity.Payment;
import com.example.campus_nest_backend.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/payments")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;

    @PostMapping("/initialize")
    public ResponseEntity<Map<String, Object>> initializePayment(@RequestBody Map<String, Object> request) throws Exception {
        String email = (String) request.get("email");
        Integer amount = (Integer) request.get("amount");
        Long bookingId = (Long) request.get("bookingId");
        return ResponseEntity.ok(paymentService.initializePayment(email, amount, bookingId));
    }

    @GetMapping("/verify/{reference}")
    public ResponseEntity<Payment> verifyPayment(@PathVariable String reference) throws Exception {
        return ResponseEntity.ok(paymentService.verifyAndProcessPayment(reference));
    }

    @PostMapping("/webhook")
    public ResponseEntity<String> handleWebhook(@RequestBody Map<String, Object> payload,
                                                @RequestHeader("x-paystack-signature") String signature) throws Exception {
        String result = paymentService.handleWebhook(payload, signature);
        return ResponseEntity.ok(result);
    }
}
