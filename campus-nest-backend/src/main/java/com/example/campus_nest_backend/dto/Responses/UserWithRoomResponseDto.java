package com.example.campus_nest_backend.dto.Responses;

import com.example.campus_nest_backend.utils.Role;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@AllArgsConstructor
@Setter
@Getter
public class UserWithRoomResponseDto {
    private Long id;
    private String name;
    private String email;
    private String phone;
    private Role role;
    private String profilePicture;
    private LocalDateTime dateJoined;
    private RoomSummaryDto currentRoom;
}