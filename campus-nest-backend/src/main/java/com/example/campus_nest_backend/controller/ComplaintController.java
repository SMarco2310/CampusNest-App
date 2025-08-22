package com.example.campus_nest_backend.controller;

import com.example.campus_nest_backend.entity.Complaint;
import com.example.campus_nest_backend.entity.Student;
import com.example.campus_nest_backend.service.ComplaintService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/complaints")
@RequiredArgsConstructor
public class ComplaintController {

    private final ComplaintService complaintService;

    // Create a complaint
    @PostMapping
    public ResponseEntity<Complaint> createComplaint(@RequestBody Complaint complaint) {
        Complaint saved = complaintService.createComplaint(complaint);
        return ResponseEntity.ok(saved);
    }

    // Get all complaints
    @GetMapping
    public ResponseEntity<List<Complaint>> getAllComplaints() {
        return ResponseEntity.ok(complaintService.getAllComplaints());
    }

    // Get complaints by student
    @GetMapping("/student/{studentId}")
    public ResponseEntity<List<Complaint>> getComplaintsByStudent(@PathVariable Long studentId) {
        Student student = new Student();
        student.setId(studentId); // assuming only id is needed for query
        return ResponseEntity.ok(complaintService.getComplaintsByStudent(student));
    }

    // Update complaint (status/response)
    @PutMapping("/{id}")
    public ResponseEntity<Complaint> updateComplaint(
            @PathVariable Long id,
            @RequestBody Complaint updatedComplaint) {

        return complaintService.getComplaintById(id)
                .map(complaint -> {
                    complaint.setStatus(updatedComplaint.getStatus());
                    complaint.setResponse(updatedComplaint.getResponse());
                    Complaint saved = complaintService.updateComplaint(complaint);
                    return ResponseEntity.ok(saved);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/hostel/{hostelId}")
    public ResponseEntity<List<Complaint>> getComplaintsByHostel(@PathVariable Long hostelId) {
        List<Complaint> complaints = complaintService.getComplaintsByHostel(hostelId);
        return ResponseEntity.ok(complaints);
    }
    // Delete complaint
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteComplaint(@PathVariable Long id) {
        complaintService.deleteComplaint(id);
        return ResponseEntity.noContent().build();
    }
}
