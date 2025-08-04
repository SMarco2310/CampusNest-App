package com.example.campus_nest_backend.controller;

import com.example.campus_nest_backend.dto.Requests.HostelRequest;
import com.example.campus_nest_backend.service.HostelService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/hostels")
public class HostelController {

    private final HostelService hostelService;
    public HostelController(HostelService hostelService) {
        this.hostelService = hostelService;
    }

    // This method retrieves all hostels from the database.
    @GetMapping("/hostels")
    public ResponseEntity<?> getAllHostels() {
        return  ResponseEntity.ok(Map.of("hostels",hostelService.getAllHostels()));
    }

    @GetMapping("/details/{hostelId}")
    public ResponseEntity<?> getHostelById(@PathVariable Long hostelId) {
        return   ResponseEntity.ok(Map.of("hostel",hostelService.getHostelById(hostelId)));
    }

    @PostMapping("/create")
    public ResponseEntity<?> createHostel(@RequestBody HostelRequest hostelRequest) {
        return ResponseEntity.ok(Map.of("hostel", hostelService.createHostel(hostelRequest)));
    }
    @PutMapping("/hostel/{hostelId}")
    public ResponseEntity<?> updateHostel(@PathVariable Long hostelId, HostelRequest hostelRequest) {
        return ResponseEntity.ok(Map.of("hostel",hostelService.updateHostel(hostelId, hostelRequest)));
    }
    @DeleteMapping("/hostel/{hostelId}")
    public ResponseEntity<?> deleteHostel(@PathVariable Long hostelId) {
        hostelService.deleteHostel(hostelId);
        return ResponseEntity.ok(Map.of("message","Hostel deleted successfully"));
    }

}
