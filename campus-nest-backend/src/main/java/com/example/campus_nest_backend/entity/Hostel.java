package com.example.campus_nest_backend.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "hostels")
public class Hostel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotBlank(message = "Hostel name is required")
    @Size(min = 2, max = 100, message = "Hostel name must be between 2 and 100 characters")
    @Column(nullable = false, name = "hostel_name")
    private String hostelName;

    @NotBlank(message = "Location is required")
    @Size(max = 500, message = "Location cannot exceed 500 characters")
    @Column(nullable = false, name = "location")
    private String location;

    @NotBlank(message = "Description is required")
    @Size(max = 2000, message = "Description cannot exceed 2000 characters")
    @Column(nullable = false, name = "description", length = 2000)
    private String description;

    @ElementCollection
    @CollectionTable(name = "hostel_image_urls", joinColumns = @JoinColumn(name = "hostel_id"))
    @Column(name = "image_url")
    private List<String> hostelPictures = new ArrayList<>();

    @OneToMany(mappedBy = "hostel", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Room> rooms = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "manager_id")
    private Hostel_Manager manager;


    @OneToMany(mappedBy = "hostel", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Review> reviews = new ArrayList<>();

    @Min(value = 0, message = "Total rooms cannot be negative")
    @Column(nullable = false, name = "total_rooms")
    private int totalRooms = 0;

    @CreatedDate
    @Column(name = "created_at", updatable = false)
    private LocalDate createdAt;

    @LastModifiedDate
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    // Optional fields
    @Column(name = "check_in_time")
    private LocalDateTime checkInTime;

    @Column(name = "check_out_time")
    private LocalDateTime checkOutTime;

    @OneToMany(mappedBy = "hostel", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<BankAccountDetails> bankAccountDetails = new ArrayList<>();

    public Hostel() {
        this.createdAt = LocalDate.now();
    }

    public Hostel(String hostelName, String location, String description, List<String> hostelPictures) {
        this();
        this.hostelName = hostelName;
        this.location = location;
        this.description = description;
        this.hostelPictures = hostelPictures != null ? hostelPictures : new ArrayList<>();
    }

    public int getAvailableRooms() {
        return (int) rooms.stream()
                .filter(Room::hasAvailableSpace)
                .count();
    }

    public int getCurrentOccupancy() {
        return rooms.stream()
                .mapToInt(Room::getCurrentOccupancy)
                .sum();
    }

    public int getTotalOccupancy() {
        return rooms.stream()
                .mapToInt(Room::getCapacity)
                .sum();
    }

    public BigDecimal getMinPrice() {
        return rooms.stream()
                .filter(Room::hasAvailableSpace)
                .map(Room::getPricePerBed)
                .min(BigDecimal::compareTo)
                .orElse(BigDecimal.ZERO);
    }

    public BigDecimal getMaxPrice() {
        return rooms.stream()
                .filter(Room::hasAvailableSpace)
                .map(Room::getPricePerBed)
                .max(BigDecimal::compareTo)
                .orElse(BigDecimal.ZERO);
    }

    public BigDecimal getAverageRatings() {
        if (reviews.isEmpty()) {
            return BigDecimal.ZERO;
        }
        double average = reviews.stream()
                .mapToInt(Review::getRating)
                .average()
                .orElse(0.0);
        return BigDecimal.valueOf(average);
    }

    public void setAvailableRooms(int availableRoomsCount) {
        if (availableRoomsCount < 0) {
            throw new IllegalArgumentException("Available rooms count cannot be negative");
        }
        this.totalRooms = availableRoomsCount;
    }
}