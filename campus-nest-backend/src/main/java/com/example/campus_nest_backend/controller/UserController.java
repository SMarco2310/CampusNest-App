package com.example.campus_nest_backend.controller;


import com.example.campus_nest_backend.entity.User;
import com.example.campus_nest_backend.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final  UserService userService;

    public UserController(UserService userService) {

        this.userService = userService;
    }

    // This method retrieves all users from the database.

    @GetMapping("/users")
    public ResponseEntity<List<User>> getAllUsers() {

        return ResponseEntity.ok(userService.getAllUsers()); // Return 200 OK with the list of users
    }

    // This method retrieves a user by their ID.
    @GetMapping("/{userId}")
    public ResponseEntity<User> getUserById( @PathVariable String userId) {
        return ResponseEntity.ok(userService.getUserById(Long.valueOf(userId)));
    }
    // This method deletes a user by their ID.
    @DeleteMapping("/delete/{userId}")
    public ResponseEntity<Void> deleteUser(@PathVariable String userId) {
        userService.deleteUser(Long.valueOf(userId));
        return ResponseEntity.noContent().build(); // Return 204 No Content after deletion
    }

    @PutMapping("/update/{userid}")
    public ResponseEntity<User> updateUser(@PathVariable String userId, User userDetails) {
        return ResponseEntity.ok(userService.updateUser(Long.valueOf(userId), userDetails));
    }
}
