package com.example.campus_nest_backend.dto.Responses;

import com.example.campus_nest_backend.utils.Capacity;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;
@Data
@Setter
@Getter
public class RoomResponseDto {
    private Long id;
    private String roomNumber;
    private Capacity roomCapacity;
    private int capacity;
    private int currentOccupancy;
    private BigDecimal pricePerBed;
    private String description;
    private boolean status;
    private List<String> roomPictures;
    private List<String> amenities;
    private int floor;
    private boolean hasAvailableSpace;
}
