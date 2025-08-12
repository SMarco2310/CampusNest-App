package com.example.campus_nest_backend.controller;

import com.example.campus_nest_backend.dto.Requests.PasswordUpdateRequestDto;
import com.example.campus_nest_backend.dto.Requests.UserUpdateRequestDto;
import com.example.campus_nest_backend.dto.Responses.ApiResponse;
import com.example.campus_nest_backend.dto.Responses.UserResponseDto;
import com.example.campus_nest_backend.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<UserResponseDto>>> getAllUsers() {
        List<UserResponseDto> users = userService.getAllUsers();
        return ResponseEntity.ok(
                new ApiResponse<>(HttpStatus.OK.value(), true, "Users fetched successfully", users)
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<UserResponseDto>> getUserById(@PathVariable Long id) {
        UserResponseDto user = userService.getUserById(id);
        return ResponseEntity.ok(
                new ApiResponse<>(HttpStatus.OK.value(), true, "User fetched successfully", user)
        );
    }

    @PutMapping("/{id}/password")
    public ResponseEntity<ApiResponse<Void>> updatePassword(
            @PathVariable Long id,
            @RequestBody PasswordUpdateRequestDto passwordUpdateRequest
    ){
        userService.updatePassword(id, passwordUpdateRequest);
        return ResponseEntity.ok(
                new ApiResponse<>(HttpStatus.OK.value(), true, "Password updated successfully", null)
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<UserResponseDto>> updateUser(
            @PathVariable Long id,
            @RequestBody UserUpdateRequestDto updatedUser
    ) {
        UserResponseDto user = userService.updateUser(id, updatedUser);
        return ResponseEntity.ok(
                new ApiResponse<>(HttpStatus.OK.value(), true, "User updated successfully", user)
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT)
                .body(new ApiResponse<>(HttpStatus.NO_CONTENT.value(), true, "User deleted successfully", null));
    }
}
