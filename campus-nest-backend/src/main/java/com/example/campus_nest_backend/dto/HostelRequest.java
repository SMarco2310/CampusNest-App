package com.example.campus_nest_backend.dto;


import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
public class HostelRequest {

    private String name;
    private String address;
    private String description;
    private Long managerId;
    private List<String> imageUrls = new ArrayList<>();
}
