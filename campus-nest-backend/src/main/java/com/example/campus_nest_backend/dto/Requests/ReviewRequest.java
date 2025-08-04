package com.example.campus_nest_backend.dto.Requests;


import lombok.Getter;
import lombok.Setter;

@Setter
@Getter

public class ReviewRequest {
    private String comment;
    private int rating;
    private Long user;
    private Long hostel;
}
