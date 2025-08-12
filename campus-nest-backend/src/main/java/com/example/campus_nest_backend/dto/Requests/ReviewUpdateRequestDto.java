package com.example.campus_nest_backend.dto.Requests;


import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ReviewUpdateRequestDto {
    private String comment;
    private int rating;
}