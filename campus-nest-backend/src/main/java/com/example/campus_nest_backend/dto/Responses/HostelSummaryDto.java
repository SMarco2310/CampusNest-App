package com.example.campus_nest_backend.dto.Responses;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@Data
@Getter
@Setter
public class HostelSummaryDto {
    private Long id;
    private String hostelName;
    private String location;
    private List<String> hostelPictures;
    private BigDecimal minPrice;
    private BigDecimal averageRating;
    private int availableRooms;
}
