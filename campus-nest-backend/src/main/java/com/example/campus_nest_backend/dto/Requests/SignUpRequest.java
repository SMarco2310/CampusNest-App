package com.example.campus_nest_backend.dto.Requests;

import jakarta.validation.constraints.Email;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class SignUpRequest {
    private String name; // User's full name
    @Email
    private String email; // User's email address
    private String password; // User's password
    private String role; // User's role (e.g., STUDENT, ADMIN, HOSTEL_MANAGER)
    private String phone; // User's phone number

}
