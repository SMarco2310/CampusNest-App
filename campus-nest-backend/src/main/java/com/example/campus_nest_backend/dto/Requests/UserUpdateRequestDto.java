package com.example.campus_nest_backend.dto.Requests;


import jakarta.annotation.Nullable;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@AllArgsConstructor
@Setter
@Getter
public class UserUpdateRequestDto {
    private String name;
    private String phone;
    private String profilePicture;
    @Nullable
    private Long StudentId;
    @Nullable
    private String course;
    private Integer classYear;

}