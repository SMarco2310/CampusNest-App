package com.example.campus_nest_backend.dto.Responses;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@AllArgsConstructor
public class HostelResponse {
    private Long id;
    private String name;
    private String address;
    private String description;
//    private double rating;
    private int availableRooms;
    private int totalRooms;
    private Long managerId;
    private List<String> imageUrls;
}
