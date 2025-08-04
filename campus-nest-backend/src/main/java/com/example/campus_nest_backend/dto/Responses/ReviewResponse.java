package com.example.campus_nest_backend.dto.Responses;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
public class ReviewResponse {
    private Long id;
    private String comment;
    private double rating;
    private Long userId;
    private Long hostelId;
    private String userProfileImageUrl; // Assuming you want to include the user's profile image URL
    private String userName; // Assuming you want to include the user's name
    private String hostelName; // Assuming you want to include the hostel's name
}
