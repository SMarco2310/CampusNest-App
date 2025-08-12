package com.example.campus_nest_backend.dto.Responses;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ManagerResponseDto {

    private Long Id;
    private String Name;
    private String Email;
    private String PhoneNumber;
    private String ProfileImageUrl;

}
