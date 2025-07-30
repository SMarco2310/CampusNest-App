package com.example.campus_nest_backend.controller;


import com.example.campus_nest_backend.dto.BookingRequest;
import com.example.campus_nest_backend.entity.Booking;
import com.example.campus_nest_backend.service.BookingService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Controller("/api/bookings")
public class BookingController {

    private final BookingService bookingService;

    public BookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    @GetMapping("/bookings")
    public ResponseEntity<?> getBookings() {
        return ResponseEntity.ok(Map.of("bookings",bookingService.getAllBookings()));
    }

    @GetMapping("/bookings/{userId}")
    public ResponseEntity<List<Booking>> getBookings( @Valid @PathVariable Long userId) {
        return ResponseEntity.ok(bookingService.findBookingsByUserId(userId));
    }

    @PostMapping("/booking")
    public ResponseEntity<?> createBooking(@Valid @RequestBody BookingRequest bookingRequest) {
        return ResponseEntity.ok(Map.of("booking",bookingService.bookRoom(bookingRequest),"message","Room successfully booked"));
    }

    @PutMapping("/update/booking/{id}")
    public ResponseEntity<?> updateBooking(@PathVariable Long id, @Valid @RequestBody BookingRequest bookingRequest) {
        return ResponseEntity.ok(Map.of("booking",bookingService.updateBooking(id, bookingRequest),"message","Booking successfully updated"));
    }

    @DeleteMapping("/delete/booking/{id}")
    public ResponseEntity<?> deleteBooking(@PathVariable Long id) {
        bookingService.deleteBooking(id);
        return ResponseEntity.ok(Map.of("message", "booking has been deleted"));
    }
}
