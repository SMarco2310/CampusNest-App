package com.example.campus_nest_backend.dto;

import com.example.campus_nest_backend.utils.Capacity;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class NewRoomRequest {
    private String roomNumber;
    private int capacity;
    private int floor;
    private double pricePerBed;
    private String description;
    private Long hostelId;
    private List<String> amenities;
    private List<String> images;
}
