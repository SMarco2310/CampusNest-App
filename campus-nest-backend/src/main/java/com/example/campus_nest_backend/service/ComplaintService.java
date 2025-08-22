package com.example.campus_nest_backend.service;

import com.example.campus_nest_backend.entity.Complaint;
import com.example.campus_nest_backend.entity.Student;
import com.example.campus_nest_backend.repository.ComplaintRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ComplaintService {

    private final ComplaintRepository complaintRepository;

    // Create a new complaint
    public Complaint createComplaint(Complaint complaint) {
        return complaintRepository.save(complaint);
    }

    // Get all complaints
    public List<Complaint> getAllComplaints() {
        return complaintRepository.findAll();
    }

    // Get complaints by student
    public List<Complaint> getComplaintsByStudent(Student student) {
        return complaintRepository.findByStudent(student);
    }

    // Get complaint by id
    public Optional<Complaint> getComplaintById(Long id) {
        return complaintRepository.findById(id);
    }

    // Update complaint (status or response)
    public Complaint updateComplaint(Complaint complaint) {
        return complaintRepository.save(complaint);
    }

    // Delete complaint
    public void deleteComplaint(Long id) {
        complaintRepository.deleteById(id);
    }

    public List<Complaint> getComplaintsByHostel(Long hostel_id) {
        return complaintRepository.findByHostel_Id(hostel_id);
    }
}
