package com.example.campus_nest_backend.repository;

import com.example.campus_nest_backend.entity.Complaint;
import com.example.campus_nest_backend.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ComplaintRepository extends JpaRepository<Complaint, Long> {
    List<Complaint> findByStudent(Student student);

    // New method to find complaints by hostel
    List<Complaint> findByHostel_Id(Long hostel_id);
}
