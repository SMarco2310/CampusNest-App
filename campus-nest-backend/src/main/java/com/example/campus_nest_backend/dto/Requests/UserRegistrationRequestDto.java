package com.example.campus_nest_backend.dto.Requests;

import com.example.campus_nest_backend.utils.Role;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@AllArgsConstructor
@Setter
@Getter
public class UserRegistrationRequestDto {
    private String name;
    private String email;
    private String password;
    private String phone;
    private Role role;
    private String profilePicture;
}