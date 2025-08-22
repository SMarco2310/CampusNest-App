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
// Booking summary
public class BookingSummaryDto {
    private Long id;
    private LocalDateTime checkInDate;
    private LocalDateTime checkOutDate;
    private Status status;
    private BigDecimal totalAmount;
    private String roomNumber;
    private String hostelName;
}