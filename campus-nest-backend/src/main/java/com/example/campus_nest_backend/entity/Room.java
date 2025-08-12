package com.example.campus_nest_backend.entity;

import com.example.campus_nest_backend.utils.Capacity;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "rooms")
public class Room {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotBlank(message = "Room number is required")
    @Size(max = 10, message = "Room number cannot exceed 10 characters")
    @Column(nullable = false, unique = true, name = "room_number")
    private String roomNumber;

    @NotNull(message = "Room capacity is required")
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, name = "room_capacity")
    private Capacity roomCapacity;

    @Min(value = 1, message = "Capacity must be at least 1")
    @Max(value = 10, message = "Capacity cannot exceed 10")
    @Column(nullable = false, name = "capacity")
    private int capacity;

    @DecimalMin(value = "0.0", message = "Price cannot be negative")
    @NotNull(message = "Price per bed is required")
    @Column(nullable = false, name = "price_per_bed", precision = 10, scale = 2)
    private BigDecimal pricePerBed;

    @NotBlank(message = "Description is required")
    @Size(max = 1000, message = "Description cannot exceed 1000 characters")
    @Column(nullable = false, name = "description", length = 1000)
    private String description;

    @Column(nullable = false, name = "status")
    private boolean status = true;

    @ElementCollection
    @CollectionTable(name = "room_image_urls", joinColumns = @JoinColumn(name = "room_id"))
    @Column(name = "image_url")
    private List<String> roomPictures = new ArrayList<>();

    @NotNull(message = "Hostel is required")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "hostel_id", nullable = false)
    private Hostel hostel;

    @OneToMany(mappedBy = "currentRoom", cascade = CascadeType.ALL)
    private List<User> occupants = new ArrayList<>();

    @ElementCollection
    @CollectionTable(name = "room_amenities", joinColumns = @JoinColumn(name = "room_id"))
    @Column(name = "amenity")
    private List<String> amenities = new ArrayList<>();

    @Min(value = 0, message = "Floor cannot be negative")
    @Column(nullable = false, name = "floor")
    private int floor;

    @Min(value = 0, message = "Current occupancy cannot be negative")
    @Column(nullable = false, name = "current_occupancy")
    private int currentOccupancy = 0;

    @Column(nullable = false, name = "is_available")
    private boolean isAvailable = true;

    public Room() {}

    public Room(String roomNumber, Capacity roomCapacity, BigDecimal pricePerBed, String description, List<String> roomPictures) {
        this.roomNumber = roomNumber;
        this.roomCapacity = roomCapacity;
        this.capacity = roomCapacity.getValue();
        this.pricePerBed = pricePerBed;
        this.description = description;
        this.roomPictures = roomPictures != null ? roomPictures : new ArrayList<>();
    }

    public boolean hasAvailableSpace() {
        return currentOccupancy < capacity && status;
    }

    public boolean addOccupant(User occupant) {
        if (hasAvailableSpace()) {
            occupants.add(occupant);
            currentOccupancy++;
            occupant.setCurrentRoom(this);
            return true;
        }
        return false;
    }

    public void removeOccupant(User occupant) {
        if (occupants.remove(occupant)) {
            currentOccupancy--;
            occupant.setCurrentRoom(null);
        }
    }

    public int getAvailableSpaces() {
        return Math.max(0, capacity - currentOccupancy);
    }

    public boolean isFull() {
        return currentOccupancy >= capacity;
    }


}