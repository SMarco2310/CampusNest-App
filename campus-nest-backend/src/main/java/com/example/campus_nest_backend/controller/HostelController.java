package com.example.campus_nest_backend.controller;

import com.example.campus_nest_backend.dto.Requests.HostelCreateRequestDto;
import com.example.campus_nest_backend.dto.Requests.HostelUpdateRequestDto;
import com.example.campus_nest_backend.dto.Responses.*;
import com.example.campus_nest_backend.service.HostelService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/hostels")
public class HostelController {

    private final HostelService hostelService;

    public HostelController(HostelService hostelService) {
        this.hostelService = hostelService;
    }

    @PostMapping
    public ResponseEntity<ApiResponse<HostelResponseDto>> createHostel(@Valid @RequestBody HostelCreateRequestDto request) {
        HostelResponseDto createdHostel = hostelService.createHostel(request);
        ApiResponse<HostelResponseDto> response = new ApiResponse<>(
                HttpStatus.CREATED.value(),
                true,
                "Hostel created successfully",
                createdHostel
        );
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<HostelResponseDto>> updateHostel(
            @PathVariable Long id,
            @Valid @RequestBody HostelUpdateRequestDto request) {
        HostelResponseDto updatedHostel = hostelService.updateHostel(id, request);
        ApiResponse<HostelResponseDto> response = new ApiResponse<>(
                HttpStatus.OK.value(),
                true,
                "Hostel updated successfully",
                updatedHostel
        );
        return ResponseEntity.ok(response);
    }

    @GetMapping("/summary")
    public ResponseEntity<ApiResponse<List<HostelSummaryDto>>> getAllHostelsSummary() {
        List<HostelSummaryDto> hostels = hostelService.getAllHostelsSummary();
        ApiResponse<List<HostelSummaryDto>> response = new ApiResponse<>(
                HttpStatus.OK.value(),
                true,
                "Hostels retrieved successfully",
                hostels
        );
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<HostelDetailResponseDto>> getHostelDetail(@PathVariable Long id) {
        HostelDetailResponseDto hostelDetail = hostelService.getHostelDetailById(id);
        ApiResponse<HostelDetailResponseDto> response = new ApiResponse<>(
                HttpStatus.OK.value(),
                true,
                "Hostel detail retrieved successfully",
                hostelDetail
        );
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteHostel(@PathVariable Long id) {
        hostelService.deleteHostel(id);
        ApiResponse<Void> response = new ApiResponse<>(
                HttpStatus.OK.value(),
                true,
                "Hostel deleted successfully",
                null
        );
        return ResponseEntity.ok(response);
    }
}
