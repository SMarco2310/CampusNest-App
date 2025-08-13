package com.example.campus_nest_backend.dto.Responses;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Setter
@Getter
public class UserSummaryDto {
    private Long id;
    private String name;
    private String email;
    private String profilePicture;
}
