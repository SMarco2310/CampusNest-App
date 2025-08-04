package com.example.campus_nest_backend.dto.Requests;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
public class UpdateUserRequest {
    private String name;
    @Email
    private String email;

    private String phone;
    private String password;
    private String profilePicture;
}
