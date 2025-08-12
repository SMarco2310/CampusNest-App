package com.example.campus_nest_backend.dto.Requests;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
@Setter
@Getter
@Data
public class BookingCreateRequestDto {
    private Long roomId;
    private LocalDate checkInDate;
    private LocalDate checkOutDate;
    private Long userId;
    private int durationMonths;
}
