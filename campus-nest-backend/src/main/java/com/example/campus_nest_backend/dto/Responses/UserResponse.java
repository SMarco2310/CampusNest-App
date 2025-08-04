package com.example.campus_nest_backend.dto.Responses;


import com.example.campus_nest_backend.utils.Role;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
public class UserResponse {

    private Long id;
    private String name;
    private String email;
    private String phoneNumber;
    private Role role; // e.g., "USER", "ADMIN"
    private String profilePicture;

}
