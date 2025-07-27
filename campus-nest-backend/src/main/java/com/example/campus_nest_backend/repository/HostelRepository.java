package com.example.campus_nest_backend.repository;

import com.example.campus_nest_backend.entity.Hostel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HostelRepository extends JpaRepository<Hostel,Long> {

   Hostel getHostelById(Long id);

   ;
}
