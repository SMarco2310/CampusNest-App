package com.example.campus_nest_backend.dto.Requests;

import java.math.BigDecimal;
import java.util.List;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;



@Data
@Getter
@Setter
public class RoomUpdateRequestDto {
    private BigDecimal pricePerBed;
    private String description;
    private List<String> roomPictures;
    private List<String> amenities;
    private boolean status;
}