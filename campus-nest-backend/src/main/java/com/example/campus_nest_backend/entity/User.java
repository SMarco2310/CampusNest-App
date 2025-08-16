package com.example.campus_nest_backend.entity;

import com.example.campus_nest_backend.utils.Gender;
import com.example.campus_nest_backend.utils.Role;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.time.LocalDateTime;


@Getter
@Setter
@Entity
@Table(name = "users")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE) // Single table for all user types
@DiscriminatorColumn(name = "user_type", discriminatorType = DiscriminatorType.STRING)

public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotBlank(message = "Name is required")
    @Size(min = 2, max = 50, message = "Name must be between 2 and 50 characters")
    @Column(nullable = false, name = "name")
    private String name;

    @JsonIgnore
    @NotBlank(message = "Password is required")
    @Size(min = 6, message = "Password must be at least 6 characters")
    @Column(nullable = false, name = "password")
    private String password;

    @NotBlank(message = "Phone number is required")
    @Pattern(regexp = "^\\+?[1-9]\\d{1,14}$", message = "Invalid phone number format")
    @Column(nullable = false, name = "phone")
    private String phone;

    @Email(message = "Invalid email format")
    @NotBlank(message = "Email is required")
    @Column(nullable = false, unique = true, name = "email")
    private String email;

    @NotNull(message = "Role is required")
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, name = "role")
    private Role role;

    @Enumerated(EnumType.STRING)
    @Column(name="gender", nullable = false)
    private Gender gender; // FEMALE,MALE

    @CreatedDate
    @Column(name = "date_joined", updatable = false)
    private LocalDateTime dateJoined;


    @Column(name = "profile_picture")
    private String profilePicture = null; // Default profile picture


    public User() {
        this.dateJoined = LocalDateTime.now();
    }

    public User(String name, String password, String phone, String email) {
        this();
        this.name = name;
        this.password = password;
        this.phone = phone;
        this.email = email;
    }

    public void updatePassword(String newPassword) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        this.password = encoder.encode(newPassword);
    }



    public boolean isStudent() {
        return this.role == Role.STUDENT;
    }

    public boolean isManager() {
        return this.role == Role.HOSTEL_MANAGER;
    }
}