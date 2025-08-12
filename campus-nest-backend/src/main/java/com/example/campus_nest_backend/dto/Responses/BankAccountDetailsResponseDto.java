package com.example.campus_nest_backend.dto.Responses;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BankAccountDetailsResponseDto {
    private String accountName;
    private String bankName;
    private String currency;
    private String accountNumber;
}