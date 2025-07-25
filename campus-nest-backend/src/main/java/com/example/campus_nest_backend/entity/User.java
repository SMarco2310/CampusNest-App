package com.example.campus_nest_backend.entity;


import com.example.campus_nest_backend.utils.Role;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, name = "Name")
    private String name;
    @Column(nullable = false, unique = true, name = "Email")
    private String email;
    @Column(nullable = false, name = "Password")
    private String password;
    @Column(nullable = false, name = "Phone")
    private String phone;
    @Column(nullable = false, name = "Role")
    private Role role = Role.STUDENT; // Default role is STUDENT
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Booking> bookings = new ArrayList<>();
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Review> reviews = new ArrayList<>();
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "room_id")
    private Room room;


    public User() {
        // Default constructor
    }
}
