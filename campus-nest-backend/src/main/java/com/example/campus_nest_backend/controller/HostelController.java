package com.example.campus_nest_backend.controller;

import com.example.campus_nest_backend.dto.HostelRequest;
import com.example.campus_nest_backend.entity.Hostel;
import com.example.campus_nest_backend.repository.HostelRepository;
import com.example.campus_nest_backend.service.HostelService;
import com.example.campus_nest_backend.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller("api/hostels")
public class HostelController {

    private final HostelService hostelService;
    public HostelController(HostelService hostelService) {
        this.hostelService = hostelService;
    }

    // This method retrieves all hostels from the database.
    @GetMapping("/hostels")
    public ResponseEntity<List<Hostel>> getAllHostels() {
        return  ResponseEntity.ok(hostelService.getAllHostels());
    }

    @GetMapping("/details/{hostelId}")
    public ResponseEntity<Hostel> getHostelById(@PathVariable Long hostelId) {
        return   ResponseEntity.ok(hostelService.getHostelById(hostelId));
    }

    @PostMapping("/create")
    public ResponseEntity<Hostel> createHostel(HostelRequest hostelRequest) {
        return ResponseEntity.ok(hostelService.createHostel(hostelRequest));
    }
    @PutMapping("/hostel/{hostelId}")
    public ResponseEntity<Hostel> updateHostel(@PathVariable Long hostelId, HostelRequest hostelRequest) {
        return ResponseEntity.ok(hostelService.updateHostel(hostelId, hostelRequest));
    }
    @DeleteMapping("/hostel/{hostelId}")
    public ResponseEntity<?> deleteHostel(@PathVariable Long hostelId) {
        hostelService.deleteHostel(hostelId);
        return ResponseEntity.ok("Hostel deleted successfully");
    }

}
