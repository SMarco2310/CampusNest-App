package com.example.campus_nest_backend.repository;

import com.example.campus_nest_backend.entity.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository

public interface RoomRepository extends JpaRepository<Room, Long> {
}
