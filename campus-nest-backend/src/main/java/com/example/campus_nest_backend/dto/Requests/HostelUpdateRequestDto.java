package com.example.campus_nest_backend.dto.Requests;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class HostelUpdateRequestDto {
    private String hostelName;
    private String location;
    private String description;
    private List<String> hostelPictures;
}