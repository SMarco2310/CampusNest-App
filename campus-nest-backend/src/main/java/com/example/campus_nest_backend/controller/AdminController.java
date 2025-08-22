package com.example.campus_nest_backend.controller;

import com.example.campus_nest_backend.dto.Requests.UserLoginRequestDto;
import com.example.campus_nest_backend.dto.Requests.UserRegistrationRequestDto;
import com.example.campus_nest_backend.dto.Responses.ApiResponse;
import com.example.campus_nest_backend.dto.Responses.LoginResponseDto;
import com.example.campus_nest_backend.dto.Responses.UserResponseDto;
import com.example.campus_nest_backend.entity.User;
import com.example.campus_nest_backend.security.JwtService;
import com.example.campus_nest_backend.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminController {

    private final UserService userService;
    private final JwtService jwtService;

    // 1️⃣ Create an Admin
    @PostMapping("/register")
    @PreAuthorize("hasRole('ADMIN')") // only admins can call this
    public ResponseEntity<ApiResponse<?>> register(@RequestBody UserRegistrationRequestDto registrationRequest) {
        User user = userService.createUser(registrationRequest);
        String token = jwtService.generateToken(user.getEmail(), user.getRole().name());

        return ResponseEntity.status(HttpStatus.CREATED).body(new ApiResponse<>(
                HttpStatus.CREATED.value(),
                true,
                "Admin registered successfully",
                new LoginResponseDto(token, user.getId())
        ));
    }

    @PostMapping("/login")
    @PreAuthorize("hasRole('ADMIN')") // only admins can call this
    public ResponseEntity<ApiResponse<?>> login(@RequestBody UserLoginRequestDto loginRequest) {
        try {
            User user = userService.authenticateUser(loginRequest);
            String token = jwtService.generateToken(user.getEmail(), user.getRole().name());

            return ResponseEntity.ok(new ApiResponse<>(
                    HttpStatus.OK.value(),
                    true,
                    "Admin logged in successfully",
                    new LoginResponseDto(token, user.getId())
            ));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ApiResponse<>(
                    HttpStatus.UNAUTHORIZED.value(),
                    false,
                    "Invalid email or password",
                    null
            ));
        }
    }
    // 2️⃣ Get all Admins
    @GetMapping("/all")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<UserResponseDto>> getAllAdmins() {
        return ResponseEntity.ok(userService.getAllAdmins());
    }

    // 3️⃣ Delete a user (admin-only action)
    @DeleteMapping("/user/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.ok("User deleted successfully");
    }

    @GetMapping("/Managers")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<List<UserResponseDto>>> getAllManagers() {
        List<UserResponseDto> managers = userService.getAllManagers();
        return ResponseEntity.ok(
                new ApiResponse<>(HttpStatus.OK.value(), true, "Managers fetched successfully", managers)
        );
    }
    @GetMapping("/Students")
    @PreAuthorize("hasRole('ADMIN')")

    public ResponseEntity<ApiResponse<List<UserResponseDto>>> getAllStudents() {
        List<UserResponseDto> students = userService.getAllStudents();
        return ResponseEntity.ok(
                new ApiResponse<>(HttpStatus.OK.value(), true, "Students fetched successfully", students)
        );
    }
    // 4️⃣ Get all users (admin-only action)
    @GetMapping("/users")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<UserResponseDto>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }
}
