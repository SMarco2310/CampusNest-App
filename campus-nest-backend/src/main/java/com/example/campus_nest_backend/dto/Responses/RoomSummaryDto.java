package com.example.campus_nest_backend.dto.Responses;

import com.example.campus_nest_backend.utils.Capacity;
import java.math.BigDecimal;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;



@Data
@Setter
@Getter

public class RoomSummaryDto {
    private Long id;
    private String roomNumber;
    private Capacity roomCapacity;
    private BigDecimal pricePerBed;
    private boolean hasAvailableSpace;
    private int availableSpaces;
}