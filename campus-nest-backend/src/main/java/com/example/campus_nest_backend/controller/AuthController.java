package com.example.campus_nest_backend.controller;

import com.example.campus_nest_backend.dto.Requests.UserLoginRequestDto;
import com.example.campus_nest_backend.dto.Requests.UserRegistrationRequestDto;
import com.example.campus_nest_backend.dto.Responses.ApiResponse;
import com.example.campus_nest_backend.dto.Responses.LoginResponseDto;
import com.example.campus_nest_backend.entity.User;
import com.example.campus_nest_backend.security.JwtService;
import com.example.campus_nest_backend.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UserService userService;
    private final JwtService jwtService;

    public AuthController(UserService userService, JwtService jwtService) {
        this.userService = userService;
        this.jwtService = jwtService;
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<?>> login(@RequestBody UserLoginRequestDto loginRequest) {
        try {
            User user = userService.authenticateUser(loginRequest);
            String token = jwtService.generateToken(user.getEmail(), user.getRole().name());

            return ResponseEntity.ok(new ApiResponse<>(
                    HttpStatus.OK.value(),
                    true,
                    "User logged in successfully",
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

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<?>> register(@RequestBody UserRegistrationRequestDto registrationRequest) {
        User user = userService.createUser(registrationRequest);
        String token = jwtService.generateToken(user.getEmail(), user.getRole().name());

        return ResponseEntity.status(HttpStatus.CREATED).body(new ApiResponse<>(
                HttpStatus.CREATED.value(),
                true,
                "User registered successfully",
                new LoginResponseDto(token, user.getId())
        ));
    }


}
