package com.example.campus_nest_backend.repository;

import com.example.campus_nest_backend.entity.Booking;
import com.example.campus_nest_backend.utils.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BookingRepository extends JpaRepository<Booking,Long> {

    List<Booking> findBookingsByStudent_Id(Long userId);

    List<Booking> findBookingsByRoomId(Long roomId);
    
    List<Booking> findBookingsByStatus(Status status);

    boolean existsByStudent_IdAndStatusIn(Long id, List<Status> pending);


}
