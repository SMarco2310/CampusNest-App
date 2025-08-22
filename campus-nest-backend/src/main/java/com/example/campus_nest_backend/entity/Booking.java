// Booking Entity
package com.example.campus_nest_backend.entity;

import com.example.campus_nest_backend.utils.Status;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "bookings")
public class Booking {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull(message = "Student is required")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id", nullable = false)
    private Student student;


    @NotNull(message = "Room is required")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "room_id", nullable = false)
    private Room room;

    @CreatedDate
    @Column(nullable = false, name = "booking_date", updatable = false)
    private LocalDateTime bookingDate;

    @NotNull(message = "Check-in date is required")
    @Future(message = "Check-in date must be in the future")
    @Column(nullable = false, name = "check_in_date")
    private LocalDateTime checkInDate;

    @NotNull(message = "Check-out date is required")
    @Column(nullable = false, name = "check_out_date")
    private LocalDateTime checkOutDate;

    @NotNull(message = "Status is required")
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, name = "status")
    private Status status = Status.PENDING;

    @Max(4)
    @Min(0)
    @Column(name = "payment_mode_index",nullable = false)
    private int paymentModeIndex = 0; // 0 for Paystack, 1 for Bank Transfer, etc.

    @DecimalMin(value = "0.0", message = "Total amount cannot be negative")
    @Column(nullable = false, name = "total_amount", precision = 10, scale = 2)
    private BigDecimal totalAmount = BigDecimal.ZERO;

    @Column(name = "cancellation_date")
    private LocalDateTime cancellationDate;

    @Column(name = "cancellation_reason", length = 500)
    private String cancellationReason;

    public Booking() {
        this.bookingDate = LocalDateTime.now();
    }

    public Booking(Student student, Room room, LocalDateTime checkInDate, Status status) {
        this();
        this.student = student;
        this.room = room;
        this.checkInDate = checkInDate;
        this.status = status;
    }

    @AssertTrue(message = "Check-out date must be after check-in date")
    private boolean isCheckOutAfterCheckIn() {
        return checkOutDate == null || checkInDate == null || checkOutDate.isAfter(checkInDate);
    }

    public static List<Booking> getBookings(Hostel hostel) {
        return hostel.getRooms().stream()
                .flatMap(room -> room.getOccupants().stream())
                .flatMap(user -> user.getBookings().stream())
                .filter(booking -> booking.getRoom().getHostel().equals(hostel))
                .toList();
    }

    public boolean canBeCancelled() {
        return (status == Status.PENDING || status == Status.CONFIRMED) &&
                checkInDate.isAfter(LocalDateTime.now());
    }

    public boolean isActive() {
        LocalDateTime now = LocalDateTime.now();
        return status == Status.CONFIRMED &&
                !now.isBefore(checkInDate) &&
                !now.isAfter(checkOutDate);
    }

    public long getDurationInDays() {
        return ChronoUnit.DAYS.between(checkInDate, checkOutDate);
    }

    public void cancel() {
        if (canBeCancelled()) {
            this.status = Status.CANCELLED;
            this.cancellationDate = LocalDateTime.now();
        }
    }
}

