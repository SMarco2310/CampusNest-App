package com.example.campus_nest_backend.repository;

import com.example.campus_nest_backend.entity.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookingRepository extends JpaRepository<Booking,Long> {
    List<Booking> findBookingsByUserId(Long userId);
}
