package com.example.campus_nest_backend.dto.Requests;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@Data
public class BookingUpdateRequestDto {
    private LocalDate checkInDate;
    private LocalDate checkOutDate;
    private int durationMonths;
}
