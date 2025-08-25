package com.example.campus_nest_backend.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Table(name = "bank_account_details")
@Getter
@Setter
@Entity
@AllArgsConstructor
public class BankAccountDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Account name cannot be blank")
    @Size(max = 100, message = "Account name must be at most 100 characters")
    @Column(name = "account_name", nullable = false, length = 100)
    private String accountName;

    @NotBlank(message = "Bank name cannot be blank")
    @Size(max = 100, message = "Bank name must be at most 100 characters")
    @Column(name = "bank_name", nullable = false, length = 100)
    private String bankName;

    @NotBlank(message = "Bank code cannot be blank")
    @Size(max = 20, message = "Bank code must be at most 20 characters")
    @Pattern(regexp = "^[0-9A-Za-z]+$", message = "Bank code must contain only letters and digits")
    @Column(name = "bank_code", nullable = false, length = 20)
    private String bankCode;

    @NotBlank(message = "Currency cannot be blank")
    @Size(max = 10, message = "Currency code must be at most 10 characters")
    @Pattern(regexp = "^[A-Z]{3}$", message = "Currency must be a valid 3-letter ISO code (e.g., USD, GHS)")
    @Column(name = "currency", nullable = false, length = 10)
    private String currency;

    @NotBlank(message = "Account number cannot be blank")
    @Size(min = 8, max = 34, message = "Account number must be between 8 and 34 characters")
    @Pattern(regexp = "^[0-9A-Za-z]+$", message = "Account number must contain only letters and digits")
    @Column(name = "account_number", nullable = false, length = 34)
    private String accountNumber;

    @NotNull(message = "Manager cannot be null")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "manager_id", nullable = false)
    private Hostel_Manager manager;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "hostel_id")
    private Hostel hostel;




    public BankAccountDetails() {
    }

    @AssertTrue(message = "Bank account must be linked to either a manager or a hostel, but not both")
    public boolean isLinkedToOneOwner() {
        return (manager != null) ^ (hostel != null); // XOR: true if exactly one is non-null
    }
}

