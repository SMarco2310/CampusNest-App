package com.example.campus_nest_backend.dto.Requests;

import com.example.campus_nest_backend.utils.Gender;
import com.example.campus_nest_backend.utils.Role;


import jakarta.annotation.Nullable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@Getter
@Setter
public class UserRegistrationRequestDto {
    private String name;
    private String email;
    private String password;
    private String phone;
    private Role role;
    private Gender gender;
    private String profilePicture;

    // Student-specific
    @Nullable
    private Long studentId;
    @Nullable
    private String course;
    private Integer classYear;
    @Nullable
    private Long currentRoomId; // Optional
    // Admin-specific
    @Nullable
    private List<BankAccountDetailsRequestDto> bankAccountDetails = new ArrayList<>();

}
