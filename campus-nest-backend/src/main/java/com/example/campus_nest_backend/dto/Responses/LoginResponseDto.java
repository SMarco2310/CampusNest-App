package com.example.campus_nest_backend.dto.Responses;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@AllArgsConstructor
@Setter
@Getter
public class LoginResponseDto {
    private String token;
    private Long user;
}