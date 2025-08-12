package com.example.campus_nest_backend.dto.Requests;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
public class ReviewCreateRequestDto {
    private String comment;
    private int rating;
    private Long hostelId;
    private Long userId;
}
