package com.example.campus_nest_backend.service;

import com.example.campus_nest_backend.dto.LoginRequest;
import com.example.campus_nest_backend.dto.SignUpRequest;
import com.example.campus_nest_backend.entity.User;
import com.example.campus_nest_backend.exception.UserNotFoundException;
import com.example.campus_nest_backend.repository.UserRepository;
import com.example.campus_nest_backend.utils.Role;
import com.example.campus_nest_backend.utils.utils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import com.example.campus_nest_backend.utils.utils.*;
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

    public User createUser(SignUpRequest signUpRequest) {
        User user = new User();
        // This method saves a new user to the database.
        user.setName(signUpRequest.getName());
        user.setEmail(signUpRequest.getEmail());
        user.setPassword(utils.passwordEncoder.encode(signUpRequest.getPassword()));
        user.setPhone(signUpRequest.getPhone());
        user.setRole(Role.valueOf(signUpRequest.getRole()));
        return userRepository.save(user);
    }

//    Have to go through this  again to check the password encoding and other details

    public User updateUser(Long id, User userDetails) {
        // This method updates an existing user in the database
        User user = userRepository.findById(id).orElse(null);
        if (user == null) {
            throw  new UserNotFoundException("User not found");
        }
        user.setName(userDetails.getName());
        user.setEmail(userDetails.getEmail());
        user.setPassword(userDetails.getPassword());
        user.setPhone(userDetails.getPhone());
        user.setRole(userDetails.getRole());
        user.setProfilePicture(userDetails.getProfilePicture());
        return userRepository.save(user);

    }

    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

    public User getUserById(Long id) {
        return userRepository.findById(id).orElse(null);
    }

        /*

         */

    // This method is used to load user details by username.
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        // Find the user by email
        User user = userRepository.findByEmail(email);
        // If user is not found, throw an exception
        if (userRepository.existsByEmail(email)) {
            throw new UserNotFoundException("User not found with email: " + email);
        }
        // Return the user details
        return org.springframework.security.core.userdetails.User
                .withUsername(user.getEmail())
                .password(user.getPassword())
                .roles(user.getRole().name())
                .build();
    }

    public User authenticateUser(LoginRequest loginRequest) {
        // This method authenticates a user using their email and password.
        User user = userRepository.findByEmail(loginRequest.getEmail());
        if (user == null || !utils.passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
            throw new UserNotFoundException("Invalid email or password");
        }
        return user;
    }
}
