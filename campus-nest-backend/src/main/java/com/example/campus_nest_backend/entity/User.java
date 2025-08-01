package com.example.campus_nest_backend.entity;


import com.example.campus_nest_backend.utils.Role;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.NumberFormat;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "users")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(nullable = false, name = "Name")
    private String name;

    @Email
    @Column(nullable = false, unique = true, name = "Email")
    private String email;

    @Column(nullable = false, name = "Password")
    private String password;

    @Column(nullable = false, name = "Phone")
    private String phone;

    @Column(nullable = false, name = "Role")
    private Role role = Role.STUDENT;

    @Column(nullable = true, name = "profile_picture")
    private String profilePicture;

    // Bookings by user
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Booking> bookings = new ArrayList<>();

    // Reviews by user
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Review> reviews = new ArrayList<>();

//    // Messages sent by user
//    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
//    private List<Message> messages = new ArrayList<>();
//
//    // Chats where user is client
//    @OneToMany(mappedBy = "client", cascade = CascadeType.ALL, orphanRemoval = true)
//    private List<Chat> clientChats = new ArrayList<>();
//
//    // Chats where user is manager
//    @OneToMany(mappedBy = "manager", cascade = CascadeType.ALL, orphanRemoval = true)
//    private List<Chat> managerChats = new ArrayList<>();

    // Room assigned to user (student)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "room_id")
    private Room room;

    public User() {
        // Default constructor
    }
}
