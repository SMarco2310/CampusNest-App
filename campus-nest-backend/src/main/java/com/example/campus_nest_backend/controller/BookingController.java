package com.example.campus_nest_backend.controller;

import com.example.campus_nest_backend.dto.Requests.BookingCancelRequestDto;
import com.example.campus_nest_backend.dto.Requests.BookingCreateRequestDto;
import com.example.campus_nest_backend.dto.Requests.BookingUpdateRequestDto;
import com.example.campus_nest_backend.dto.Responses.*;
import com.example.campus_nest_backend.service.BookingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/bookings")
@RequiredArgsConstructor
public class BookingController {

    private final BookingService bookingService;

    /* ------------------- CREATE ------------------- */
    @PostMapping
    public ResponseEntity<ApiResponse<BookingResponseDto>> createBooking(@RequestBody BookingCreateRequestDto request) {
        BookingResponseDto booking = bookingService.createBooking(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponse<>(201, true, "Booking created successfully", booking));
    }

    /* ------------------- GET ALL ------------------- */
    @GetMapping
    public ResponseEntity<ApiResponse<List<BookingResponseDto>>> getAllBookings() {
        List<BookingResponseDto> bookings = bookingService.getAllBookings();
        return ResponseEntity.ok(new ApiResponse<>(200, true, "All bookings retrieved", bookings));
    }

    /* ------------------- GET BY ROOM ------------------- */
    @GetMapping("/room/{roomId}")
    public ResponseEntity<ApiResponse<List<BookingResponseDto>>> getBookingsByRoom(@PathVariable Long roomId) {
        List<BookingResponseDto> bookings = bookingService.getBookingsByRoom(roomId);
        return ResponseEntity.ok(new ApiResponse<>(200, true, "Bookings for room retrieved", bookings));
    }

    /* ------------------- GET BY HOSTEL ------------------- */
    @GetMapping("/hostel/{hostelId}")
    public ResponseEntity<ApiResponse<List<BookingResponseDto>>> getBookingsByHostel(@PathVariable Long hostelId) {
        List<BookingResponseDto> bookings = bookingService.getBookingsByHostel(hostelId);
        return ResponseEntity.ok(new ApiResponse<>(200, true, "Bookings for hostel retrieved", bookings));
    }

    /* ------------------- UPDATE ------------------- */
    @PutMapping("/{bookingId}")
    public ResponseEntity<ApiResponse<BookingResponseDto>> updateBooking(
            @PathVariable Long bookingId,
            @RequestBody BookingUpdateRequestDto request,
            @RequestParam Long studentId) {

        BookingResponseDto updatedBooking = bookingService.updateBooking(bookingId, request, studentId);
        return ResponseEntity.ok(new ApiResponse<>(200, true, "Booking updated successfully", updatedBooking));
    }

    /* ------------------- CANCEL ------------------- */
    @DeleteMapping("/{bookingId}")
    public ResponseEntity<ApiResponse<Void>> cancelBooking(
            @PathVariable Long bookingId,
            @RequestParam Long studentId,
            @RequestBody BookingCancelRequestDto request) {

        bookingService.cancelBooking(bookingId, studentId, request);
        return ResponseEntity.ok(new ApiResponse<>(200, true, "Booking cancelled successfully", null));
    }

    /* ------------------- GET DETAILS ------------------- */
    @GetMapping("/{bookingId}")
    public ResponseEntity<ApiResponse<BookingDetailsResponseDto>> getBookingDetails(@PathVariable Long bookingId) {
        BookingDetailsResponseDto bookingDetails = bookingService.getBookingDetails(bookingId);
        return ResponseEntity.ok(new ApiResponse<>(200, true, "Booking details retrieved", bookingDetails));
    }

    /* ------------------- GET BY USER ------------------- */
    @GetMapping("/user/{userId}")
    public ResponseEntity<ApiResponse<List<BookingSummaryDto>>> getBookingsByUser(@PathVariable Long userId) {
        List<BookingSummaryDto> bookings = bookingService.getBookingsByUser(userId);
        return ResponseEntity.ok(new ApiResponse<>(200, true, "User bookings retrieved", bookings));
    }
}
