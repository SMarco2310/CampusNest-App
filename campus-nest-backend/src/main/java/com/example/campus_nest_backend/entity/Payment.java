package com.example.campus_nest_backend.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.math.BigDecimal;
import java.time.OffsetDateTime;

//@Getter @Setter
//@Entity
//@Table(name = "payments")
//public class Payment {
//    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long id;
//
//    @Column(nullable = false, unique = true)
//    private String paystackReference;          // transaction reference
//
//    @ManyToOne(optional = false, fetch = FetchType.LAZY)
//    private Booking booking;
//
//    @Column(nullable = false, precision = 18, scale = 2)
//    private BigDecimal amount;                 // in major currency (e.g., GHS)
//
//    @Enumerated(EnumType.STRING)
//    @Column(nullable = false)
//    private Status status = Status.PENDING;    // PENDING, PAID, PAID_OUT, FAILED
//
//    private String transferRecipientCode;      // from /transferrecipient
//    private String transferCode;               // from /transfer
//    private OffsetDateTime paidAt;
//    private OffsetDateTime paidOutAt;
//
//    public enum Status { PENDING, PAID, PAID_OUT, FAILED }
//}