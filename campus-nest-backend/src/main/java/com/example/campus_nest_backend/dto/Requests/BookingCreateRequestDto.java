package com.example.campus_nest_backend.dto.Requests;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Data
public class BookingCreateRequestDto {
    private Long roomId;
    private Long userId;
    private int paymentModeIndex; // 0 for Paystack, 1 for Bank Transfer, etc.
}
