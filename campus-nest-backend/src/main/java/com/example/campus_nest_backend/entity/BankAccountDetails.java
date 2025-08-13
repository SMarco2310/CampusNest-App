package com.example.campus_nest_backend.entity;

import jakarta.persistence.*;
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

    @Column(name = "account_name")
    private String accountName;

    @Column(name = "bank_name")
    private String bankName;

    @Column(name = "currency")
    private String currency;

    @Column(name = "account_number")
    private String accountNumber;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "manager_id", nullable = false)
    private Hostel_Manager manager;

    public BankAccountDetails() {
    }
}

