package com.example.campus_nest_backend.entity;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "hostels")
public class Hostel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, unique = true, name = "id")
    private Long id;

    @Column(nullable = false, name = "hostel_name")
    private String name;

    @Column(nullable = false, name = "address")
    private String address;

    @Column(nullable = false, name = "description")
    private String description;

    @Column(nullable = false, name = "total_rooms")
    private int totalRooms =0;

    @Column(nullable = false, name = "available_rooms")
    private int availableRooms = totalRooms; // Number of rooms available for booking

    @OneToMany(mappedBy = "hostel", cascade = CascadeType.ALL, orphanRemoval = true)
    // This field is used to track the rooms in the hostel
    private List<Room> rooms = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "manager_id")
    private User Manager; // The user who manages the hostel

    @ElementCollection
    @CollectionTable(name = "hostel_image_urls", joinColumns = @JoinColumn(name = "hostel_id"))
    @Column(name = "image_url")
    private List<String> imageUrls = new ArrayList<>();

    @OneToMany(mappedBy = "hostel", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Review> reviews = new ArrayList<>();


    public Hostel() {
    }
}

