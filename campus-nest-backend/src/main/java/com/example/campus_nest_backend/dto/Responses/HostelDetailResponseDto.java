package com.example.campus_nest_backend.dto.Responses;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Getter
@Setter
public class HostelDetailResponseDto {
    private Long id;
    private String hostelName;
    private String location;
    private String description;
    private List<String> hostelPictures;
    private int totalRooms;
    private int availableRooms;
    private int currentOccupancy;
    private int totalOccupancy;
    private BigDecimal minPrice;
    private BigDecimal maxPrice;
    private BigDecimal averageRatings;
    private int totalReviews;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalTime checkInTime;
    private LocalTime checkOutTime;
    private ManagerResponseDto manager;
    private List<RoomResponseDto> rooms;
    private List<ReviewResponseDto> reviews;
    private List<BankAccountDetailsResponseDto> bankAccountDetails;
}