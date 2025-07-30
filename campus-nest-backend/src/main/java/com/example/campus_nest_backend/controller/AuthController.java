package com.example.campus_nest_backend.controller;


import com.example.campus_nest_backend.dto.LoginRequest;
import com.example.campus_nest_backend.dto.SignUpRequest;
import com.example.campus_nest_backend.entity.User;
import com.example.campus_nest_backend.security.JwtService;
import com.example.campus_nest_backend.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UserService userService;
    private final JwtService jwtService;

    public AuthController(UserService userService, JwtService jwtService) {
        // Constructor injection for UserService
        // This allows the AuthController to use UserService methods for authentication.
        this.userService = userService;
        this.jwtService = jwtService;
    }

    // This method handles user login requests.
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        // Authenticate the user using the UserService.
        User user = userService.authenticateUser(loginRequest);
        if (user == null) {
            // If authentication fails, return an error response.
            return ResponseEntity.status(401).body(Map.of("message", "Invalid credentials"));}
        return ResponseEntity.ok(Map.of(
                "message", "User logged in successfully",
                 "token", jwtService.generateToken(user.getEmail(), user.getRole().name()),
                 "user", user.getId()));
    }

    @PostMapping("/register")
    // This method handles user registration requests.
    public ResponseEntity<?> register(@RequestBody SignUpRequest signUpRequest) {
        User user =userService.createUser(signUpRequest);
        String token =jwtService.generateToken(signUpRequest.getEmail(), signUpRequest.getRole());
        // After creating a user, generate a JWT token for the user.
        return ResponseEntity.ok(
                Map.of(
                        "message", "User registered successfully",
                        "token", token,
                        "user", user.getId()
                )
        );
    }
}


