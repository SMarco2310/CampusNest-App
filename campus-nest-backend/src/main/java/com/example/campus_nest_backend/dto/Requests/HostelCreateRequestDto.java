package com.example.campus_nest_backend.dto.Requests;

import java.time.LocalDateTime;
import java.util.List;


import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class HostelCreateRequestDto {
    @NotBlank(message = "Hostel name is required")
    @Size(min = 2, max = 100, message = "Hostel name must be between 2 and 100 characters")
    private String hostelName;

    @NotBlank(message = "Location is required")
    @Size(max = 500, message = "Location cannot exceed 500 characters")
    private String location;

    @NotBlank(message = "Description is required")
    @Size(max = 2000, message = "Description cannot exceed 2000 characters")
    private String description;

    private List<String> hostelPictures = new ArrayList<>();

    @NotNull(message = "Manager ID is required")
    private Long managerId;

    private LocalDateTime checkInTime;
    private LocalDateTime checkOutTime;

    private List<BankAccountDetailsRequestDto> bankAccountDetails = new ArrayList<>();
}