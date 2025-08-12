package com.example.campus_nest_backend.dto.Responses;

import java.time.LocalDateTime;

public class ReviewWithHostelResponseDto {
    private Long id;
    private String comment;
    private int rating;
    private LocalDateTime createdAt;
    private UserSummaryDto user;
    private HostelSummaryDto hostel;
}
