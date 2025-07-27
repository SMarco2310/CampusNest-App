package com.example.campus_nest_backend.dto;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BookingRequest {

    private Long userId; // ID of the user making the booking
    private Long roomId; // ID of the room being booked
    private String bookingDate; // Date when the booking was made, in ISO format (e.g., "2023-10-01T12:00:00Z")
}
