package com.example.campus_nest_backend.service;

import com.example.campus_nest_backend.entity.User;
import com.example.campus_nest_backend.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<User> getAllUsers() {
        // This method retrieves all users from the database.
        return userRepository.findAll();
    }

    public User getUserById(Long id) {
        // This method retrieves a user by their ID.
        return userRepository.findById(id).orElse(null);
    }


    // This method is used to load user details by username.
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        // Find the user by email
        User user = userRepository.findByEmail(email);
        // If user is not found, throw an exception
        if (userRepository.existsByEmail(email)) {
            throw new UsernameNotFoundException("User not found with email: " + email);
        }
        // Return the user details
        return org.springframework.security.core.userdetails.User
                .withUsername(user.getEmail())
                .password(user.getPassword())
                .roles(user.getRole().name())
                .build();
    }
}
