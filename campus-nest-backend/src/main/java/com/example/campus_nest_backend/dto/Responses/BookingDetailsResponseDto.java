package com.example.campus_nest_backend.dto.Responses;

import com.example.campus_nest_backend.utils.Status;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;


@Setter
@Getter
public class BookingDetailsResponseDto {
    private Long id;
    private LocalDateTime bookingDate;
    private LocalDateTime checkInDate;
    private LocalDateTime checkOutDate;
    private int durationMonths;
    private Status status;
    private BigDecimal totalAmount;
    private BigDecimal amountPaid;
    private BigDecimal remainingAmount;
    private UserSummaryDto user;
    private RoomWithHostelResponseDto room;
    private boolean canBeCancelled;
    private boolean isActive;
}
