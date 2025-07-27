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
    @GetMapping("/all")
    public ResponseEntity<List<Hostel>> getAllHostels() {
        return  ResponseEntity.ok(hostelService.getAllHostels());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Hostel> getHostelById(@PathVariable Long id) {
        return   ResponseEntity.ok(hostelService.getHostelById(id));
    }

    @PostMapping("/")
    public ResponseEntity<Hostel> createHostel(HostelRequest hostelRequest) {
        return ResponseEntity.ok(hostelService.createHostel(hostelRequest));
    }
    @PutMapping("/{id}")
    public ResponseEntity<Hostel> updateHostel(@PathVariable Long id, HostelRequest hostelRequest) {
        return ResponseEntity.ok(hostelService.updateHostel(id, hostelRequest));
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteHostel(@PathVariable Long id) {
        hostelService.deleteHostel(id);
        return ResponseEntity.ok("Hostel deleted successfully");
    }

}
