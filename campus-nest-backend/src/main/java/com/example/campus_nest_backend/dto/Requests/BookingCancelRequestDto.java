package com.example.campus_nest_backend.dto.Requests;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
public class BookingCancelRequestDto {
    private String cancellationReason;
}