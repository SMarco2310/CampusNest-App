package com.example.campus_nest_backend.dto.Responses;


import com.example.campus_nest_backend.utils.Status;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Setter
@Getter
@AllArgsConstructor
public class BookingResponseDto {
    private Long id;
    private LocalDateTime bookingDate;
    private LocalDate checkInDate;
    private LocalDate checkOutDate;
    private Status status;
    private BigDecimal totalAmount;
    private BigDecimal amountPaid;
    private BigDecimal remainingAmount;
}
