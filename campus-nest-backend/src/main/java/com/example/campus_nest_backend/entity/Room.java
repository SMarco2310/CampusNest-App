package com.example.campus_nest_backend.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "rooms")
public class Room {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, unique = true, name = "id")
    private Long id; // Unique identifier for the room
    @Column(nullable = false, name = "room_number")
    private String roomNumber; // Room number or identifier
    @Column(nullable = false, name = "capacity")
    private int capacity; // Maximum number of occupants in the room
    @Column(nullable = false, name = "current_occupancy")
    private int currentOccupancy; // Current number of occupants in the room
    @Column(nullable = false, name = "price_per_bed")
    private double pricePerBed; // Price per bed in the room
    @Column(nullable = false, name = "description")
    private String description; // Description of the room
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "hostel_id", nullable = false)
    private Hostel hostel; // The hostel to which the room belongs
    @Column(nullable = false, name = "is_available")
    private boolean isAvailable; // Availability status of the room
    // This field is used to track the occupants of the room
    @OneToMany(mappedBy = "room", cascade = CascadeType.ALL, orphanRemoval = true)
    List<User> occupants = new ArrayList<>(); // List of users currently occupying the room
    @ElementCollection
    @CollectionTable(name = "room_image_urls", joinColumns = @JoinColumn(name = "room_id"))
    @Column(name = "image_url")
    private List<String> imageUrls = new ArrayList<>();


    public Room() {
    }
}
