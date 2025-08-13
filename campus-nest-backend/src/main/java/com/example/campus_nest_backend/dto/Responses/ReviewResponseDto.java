package com.example.campus_nest_backend.dto.Responses;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class ReviewResponseDto {
    private Long id;
    private String comment;
    private int rating;
    private LocalDateTime createdAt;
    private UserSummaryDto user;
    private Long hostelId;
}
