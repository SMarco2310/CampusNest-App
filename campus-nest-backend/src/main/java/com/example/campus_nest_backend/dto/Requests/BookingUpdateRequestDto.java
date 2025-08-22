package com.example.campus_nest_backend.dto.Requests;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;


import java.time.LocalDateTime;

@Getter
@Setter
@Data
public class BookingUpdateRequestDto {
    private LocalDateTime checkInDate;
    private LocalDateTime checkOutDate;
    private int durationMonths;
    private int paymentModeIndex; // 0 for Paystack, 1 for Bank Transfer, etc.
}
