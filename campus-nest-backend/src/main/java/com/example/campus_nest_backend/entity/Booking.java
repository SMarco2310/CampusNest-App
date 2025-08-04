package com.example.campus_nest_backend.entity;

import com.example.campus_nest_backend.utils.Status;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Setter
@Getter
@Entity
@Table(name = "bookings")
public class Booking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, unique = true, name = "id")
    private Long id;

    // Many bookings can be made by one user
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "room_id", nullable = false)
    private Room room; // The room being booked

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, name = "status")
    private Status status = Status.PENDING;

    @Column(nullable = false, name = "booking_date")
    private Date bookingDate; // Date when the booking was made

    public Booking() {
    }
}
