package com.example.campus_nest_backend.dto.Requests;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BankAccountDetailsRequestDto {
    @NotBlank(message = "Account name is required")
    private String accountName;

    @NotBlank(message = "Bank name is required")
    private String bankName;

    @NotBlank(message = "Currency is required")
    private String currency;

    @NotBlank(message = "Account number is required")
    private String accountNumber;
}
