package com.example.campus_nest_backend.repository;

import com.example.campus_nest_backend.entity.Hostel;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Arrays;

@Repository

public interface HostelRepository extends JpaRepository<Hostel,Long> {

   Hostel getHostelById(Long id);

    boolean existsByManagerIdAndIdNot(Long id, Long id1);


    boolean existsByManagerId(Long id);

    boolean existsByHostelNameIgnoreCaseAndIdNot(String hostelName, Long id);

 boolean existsByHostelNameIgnoreCase(@NotBlank(message = "Hostel name is required") @Size(min = 2, max = 100, message = "Hostel name must be between 2 and 100 characters") String hostelName);

 ;
}
