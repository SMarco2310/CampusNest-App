package com.example.campus_nest_backend.controller;


import com.example.campus_nest_backend.dto.Requests.UpdateUserRequest;
import com.example.campus_nest_backend.entity.User;
import com.example.campus_nest_backend.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final  UserService userService;

    public UserController(UserService userService) {

        this.userService = userService;
    }

    // This method retrieves all users from the database.

    @GetMapping("/users")
    public ResponseEntity<?> getAllUsers() {

        return ResponseEntity.ok(Map.of("users",userService.getAllUsers())); // Return 200 OK with the list of users
    }

    // This method retrieves a user by their ID.
    @GetMapping("/{userId}")
    public ResponseEntity<?> getUserById( @PathVariable Long userId) {
        return ResponseEntity.ok(Map.of("user",userService.getUserById(userId)));
    }
    // This method deletes a user by their ID.
    @DeleteMapping("/delete/{userId}")
    public ResponseEntity<?> deleteUser(@PathVariable Long userId) {
        userService.deleteUser(userId);
        return ResponseEntity.ok(Map.of("message","User deleted successfully")); // Return 204 No Content after deletion
    }

    @PutMapping("/update/{userId}")
    public ResponseEntity<?> updateUser(@PathVariable Long userId, UpdateUserRequest userDetails) {
        return ResponseEntity.ok(Map.of("user",userService.updateUser(userId, userDetails)));
    }
}
