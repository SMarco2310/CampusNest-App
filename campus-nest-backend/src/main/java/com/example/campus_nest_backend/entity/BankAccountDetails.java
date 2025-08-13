package com.example.campus_nest_backend.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Table(name = "bank_account_details")
@Getter
@Setter
@Entity
public class BankAccountDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")

    private Long id;
    @Column(name = "account_name")
    private String accountName;

    @Column(name = "bank_name")
    private String bankName;

    @Column(name = "currency")
    private String currency;

    @Column(name = "account_number")
    private String accountNumber;

    public BankAccountDetails() {
        // Default constructor
    }
}
