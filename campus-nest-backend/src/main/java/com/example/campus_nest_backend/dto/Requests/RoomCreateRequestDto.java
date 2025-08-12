package com.example.campus_nest_backend.dto.Requests;

import com.example.campus_nest_backend.utils.Capacity;
import java.math.BigDecimal;
import java.util.List;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;



@Data
@Getter
@Setter

public class RoomCreateRequestDto {
    private String roomNumber;
    private Capacity roomCapacity;
    private int capacity;
    private BigDecimal pricePerBed;
    private String description;
    private List<String> roomPictures;
    private List<String> amenities;
    private int floor;
    private Long hostelId;
}