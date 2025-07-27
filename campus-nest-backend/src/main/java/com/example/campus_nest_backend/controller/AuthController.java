package com.example.campus_nest_backend.controller;


import com.example.campus_nest_backend.dto.LoginRequest;
import com.example.campus_nest_backend.dto.SignUpRequest;
import com.example.campus_nest_backend.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;

@Controller("api/auth")
public class AuthController {

    private final UserService userService;

    public AuthController(UserService userService) {
        // Constructor injection for UserService
        // This allows the AuthController to use UserService methods for authentication.
        this.userService = userService;
    }

    // This method handles user login requests.
    @PostMapping("/login")
    public ResponseEntity<?> login(LoginRequest loginRequest) {
         return ResponseEntity.ok(null);
    }

    @PostMapping("/register")
    // This method handles user registration requests.
    public ResponseEntity<?> register(SignUpRequest signUpRequest) {
        userService.createUser(signUpRequest);
        String token =null;
        return ResponseEntity.ok(null);
    }
}


