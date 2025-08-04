package com.example.campus_nest_backend.dto.Responses;


import com.example.campus_nest_backend.utils.Capacity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
public class RoomResponse {
    private Long id;
    private String roomNumber;
    private Capacity roomCapacity;
    private int currentOccupancy = 0;
    private double pricePerBed;
    private String description;
    private Long hostelId;
    private boolean isAvailable;

}
