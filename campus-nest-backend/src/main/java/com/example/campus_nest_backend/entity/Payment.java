package com.example.campus_nest_backend.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

@Getter
@Setter
@Entity
@Table(name = "payments",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = "paystackReference"),
        })
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Paystack reference is required")
    @Column(nullable = false, unique = true, length = 50)
    private String paystackReference; // transaction reference

    @NotNull(message = "Booking must not be null")
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Booking booking;

    @NotNull(message = "Amount must not be null")
    @DecimalMin(value = "0.01", message = "Amount must be greater than 0")
    @Digits(integer = 16, fraction = 2, message = "Amount must be a valid monetary value")
    @Column(nullable = false, precision = 18, scale = 2)
    private BigDecimal amount; // in major currency (e.g., GHS)

    @NotNull(message = "Status is required")
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private Status status = Status.PENDING; // PENDING, PAID, PAID_OUT, FAILED

    @NotBlank(message = "Currency is required")
    @Size(min = 3, max = 3, message = "Currency must be 3 letters, e.g., GHS")
    @Column(nullable = false, length = 3)
    private String currency = "GHS";

    @Size(max = 50)
    private String transferRecipientCode; // from /transferrecipient

    @Size(max = 50)
    private String transferCode; // from /transfer

    private OffsetDateTime paidAt;
    private OffsetDateTime paidOutAt;

    // Optional: automatic timestamps
    @Column(nullable = false, updatable = false)
    private OffsetDateTime createdAt = OffsetDateTime.now();

    private OffsetDateTime updatedAt;

    @PreUpdate
    public void preUpdate() {
        updatedAt = OffsetDateTime.now();
    }

    public enum Status { PENDING, PAID, PAID_OUT, FAILED }
}
