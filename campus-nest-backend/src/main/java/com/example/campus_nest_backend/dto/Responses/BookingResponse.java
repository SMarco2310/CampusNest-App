package com.example.campus_nest_backend.dto.Responses;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
public class BookingResponse {
    private Long id;
    private String userName;
    private String roomNumber;
    private String status;
    private Long userId;
    private Long roomId;
    private String BookingDate;
}
