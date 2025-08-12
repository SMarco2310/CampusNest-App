package com.example.campus_nest_backend.dto.Responses;

import com.example.campus_nest_backend.utils.Status;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;


@Setter
@Getter
// Booking summary
public class BookingSummaryDto {
    private Long id;
    private LocalDate checkInDate;
    private LocalDate checkOutDate;
    private Status status;
    private BigDecimal totalAmount;
    private String roomNumber;
    private String hostelName;
}