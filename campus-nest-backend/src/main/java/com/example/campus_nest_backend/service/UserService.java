package com.example.campus_nest_backend.service;

import com.example.campus_nest_backend.dto.Requests.PasswordUpdateRequestDto;
import com.example.campus_nest_backend.dto.Requests.UserLoginRequestDto;
import com.example.campus_nest_backend.dto.Requests.UserRegistrationRequestDto;
import com.example.campus_nest_backend.dto.Requests.UserUpdateRequestDto;
import com.example.campus_nest_backend.dto.Responses.UserResponseDto;
import com.example.campus_nest_backend.entity.User;
import com.example.campus_nest_backend.exception.DuplicateEmailException;
import com.example.campus_nest_backend.exception.UserNotFoundException;
import com.example.campus_nest_backend.repository.UserRepository;
import com.example.campus_nest_backend.utils.Role;
import com.example.campus_nest_backend.utils.utils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;

@Service
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    // ===== GET ALL USERS =====
    public List<UserResponseDto> getAllUsers() {
        return userRepository.findAll()
                .stream()
                .map(this::mapToUserResponse)
                .toList();
    }

    // ===== CREATE USER =====
    @Transactional
    public User createUser(UserRegistrationRequestDto request) {
        validateRegistrationRequest(request);

        if (userRepository.existsByEmail(request.getEmail())) {
            throw new DuplicateEmailException("Email already exists: " + request.getEmail());
        }

        User user = new User();
        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setPassword(utils.passwordEncoder.encode(request.getPassword()));
        user.setPhone(request.getPhone());
        user.setRole(request.getRole() != null ? request.getRole() : Role.STUDENT);
        user.setProfilePicture(request.getProfilePicture());

        return userRepository.save(user);
    }

    // ===== UPDATE USER =====
    @Transactional
    public UserResponseDto updateUser(Long id, UserUpdateRequestDto request) {
        if (id == null) throw new IllegalArgumentException("User ID cannot be null");

        User existingUser = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User not found with ID: " + id));

        // Partial updates
        if (StringUtils.hasText(request.getName())) existingUser.setName(request.getName());
        if (StringUtils.hasText(request.getPhone())) existingUser.setPhone(request.getPhone());
        if (request.getProfilePicture() != null) existingUser.setProfilePicture(request.getProfilePicture());

        return mapToUserResponse(userRepository.save(existingUser));
    }

    // ===== UPDATE PASSWORD =====
    @Transactional
    public void updatePassword(Long id, PasswordUpdateRequestDto request) {
        if (id == null) throw new IllegalArgumentException("User ID cannot be null");

        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User not found with ID: " + id));

        if (!utils.passwordEncoder.matches(request.getCurrentPassword(), user.getPassword())) {
            throw new IllegalArgumentException("Current password is incorrect");
        }

        if (request.getNewPassword().length() < 6) {
            throw new IllegalArgumentException("New password must be at least 6 characters long");
        }

        user.setPassword(utils.passwordEncoder.encode(request.getNewPassword()));
        userRepository.save(user);
    }

    // ===== DELETE USER =====
    @Transactional
    public void deleteUser(Long id) {
        if (id == null) throw new IllegalArgumentException("User ID cannot be null");
        if (!userRepository.existsById(id)) throw new UserNotFoundException("User not found with ID: " + id);

        userRepository.deleteById(id);
    }

    // ===== GET USER BY ID =====
    public UserResponseDto getUserById(Long id) {
        if (id == null) throw new IllegalArgumentException("User ID cannot be null");

        return userRepository.findById(id)
                .map(this::mapToUserResponse)
                .orElseThrow(() -> new UserNotFoundException("User not found with ID: " + id));
    }

    // ===== LOGIN =====
    public User authenticateUser(UserLoginRequestDto request) {
        validateLoginRequest(request);

        User user = userRepository.findByEmail(request.getEmail());
        if (user == null || !utils.passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new UserNotFoundException("Invalid email or password");
        }

        return user;
    }

    // ===== SECURITY LOAD USER =====
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        if (!StringUtils.hasText(email)) throw new IllegalArgumentException("Email cannot be empty");

        User user = userRepository.findByEmail(email);
        if (user == null) throw new UsernameNotFoundException("User not found with email: " + email);

        return org.springframework.security.core.userdetails.User
                .withUsername(user.getEmail())
                .password(user.getPassword())
                .roles(user.getRole().name())
                .accountExpired(false)
                .accountLocked(false)
                .credentialsExpired(false)
                .disabled(false)
                .build();
    }

    // ===== MAPPING =====
    private UserResponseDto mapToUserResponse(User user) {
        return new UserResponseDto(
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getPhone(),
                user.getRole(),
                user.getProfilePicture(),
                user.getDateJoined()
        );
    }

    // ===== VALIDATION =====
    private void validateRegistrationRequest(UserRegistrationRequestDto request) {
        if (request == null) throw new IllegalArgumentException("Registration request cannot be null");
        if (!StringUtils.hasText(request.getName())) throw new IllegalArgumentException("Name cannot be empty");
        if (!StringUtils.hasText(request.getEmail()) || isValidEmail(request.getEmail()))
            throw new IllegalArgumentException("Invalid email format");
        if (!StringUtils.hasText(request.getPassword()) || request.getPassword().length() < 6)
            throw new IllegalArgumentException("Password must be at least 6 characters long");
    }

    private void validateLoginRequest(UserLoginRequestDto request) {
        if (request == null) throw new IllegalArgumentException("Login request cannot be null");
        if (!StringUtils.hasText(request.getEmail())) throw new IllegalArgumentException("Email cannot be empty");
        if (!StringUtils.hasText(request.getPassword())) throw new IllegalArgumentException("Password cannot be empty");
    }

    private boolean isValidEmail(String email) {
        return email == null || !email.matches("^[A-Za-z0-9+_.-]+@([A-Za-z0-9.-]+\\.[A-Za-z]{2,})$");
    }
}
