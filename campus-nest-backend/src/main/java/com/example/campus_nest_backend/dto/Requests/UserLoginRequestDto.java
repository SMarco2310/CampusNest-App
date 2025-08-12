package com.example.campus_nest_backend.dto.Requests;



import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@AllArgsConstructor
@Setter
@Getter
public class UserLoginRequestDto {
    private String email;
    private String password;
}