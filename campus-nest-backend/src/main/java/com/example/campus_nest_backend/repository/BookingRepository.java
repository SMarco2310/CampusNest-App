package com.example.campus_nest_backend.repository;

import com.example.campus_nest_backend.entity.Booking;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookingRepository extends JpaRepository<Booking,Long> {
}
