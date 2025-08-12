package com.example.campus_nest_backend.controller;

import com.example.campus_nest_backend.dto.Requests.RoomCreateRequestDto;
import com.example.campus_nest_backend.dto.Requests.RoomUpdateRequestDto;
import com.example.campus_nest_backend.dto.Responses.ApiResponse;
import com.example.campus_nest_backend.dto.Responses.RoomResponseDto;
import com.example.campus_nest_backend.service.RoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class RoomController {

    private final RoomService roomService;

    @PostMapping("/rooms")
    public ResponseEntity<ApiResponse<RoomResponseDto>> createRoom(@RequestBody RoomCreateRequestDto request) {
        RoomResponseDto createdRoom = roomService.createRoom(request);
        ApiResponse<RoomResponseDto> response = new ApiResponse<>(
                HttpStatus.CREATED.value(),
                true,
                "Room created successfully",
                createdRoom
        );
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PutMapping("/rooms/{roomId}")
    public ResponseEntity<ApiResponse<RoomResponseDto>> updateRoom(
            @PathVariable Long roomId,
            @RequestBody RoomUpdateRequestDto request) {
        RoomResponseDto updatedRoom = roomService.updateRoom(roomId, request);
        ApiResponse<RoomResponseDto> response = new ApiResponse<>(
                HttpStatus.OK.value(),
                true,
                "Room updated successfully",
                updatedRoom
        );
        return ResponseEntity.ok(response);
    }

    @GetMapping("/rooms/{roomId}")
    public ResponseEntity<ApiResponse<RoomResponseDto>> getRoomById(@PathVariable Long roomId) {
        RoomResponseDto room = roomService.getRoomById(roomId);
        ApiResponse<RoomResponseDto> response = new ApiResponse<>(
                HttpStatus.OK.value(),
                true,
                "Room fetched successfully",
                room
        );
        return ResponseEntity.ok(response);
    }

    @GetMapping("/hostels/{hostelId}/rooms")
    public ResponseEntity<ApiResponse<List<RoomResponseDto>>> getRoomsByHostel(@PathVariable Long hostelId) {
        List<RoomResponseDto> rooms = roomService.getRoomsByHostelId(hostelId);
        ApiResponse<List<RoomResponseDto>> response = new ApiResponse<>(
                HttpStatus.OK.value(),
                true,
                "Rooms fetched successfully",
                rooms
        );
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/rooms/{roomId}")
    public ResponseEntity<ApiResponse<Void>> deleteRoom(@PathVariable Long roomId) {
        roomService.deleteRoom(roomId);
        ApiResponse<Void> response = new ApiResponse<>(
                HttpStatus.NO_CONTENT.value(),
                true,
                "Room deleted successfully",
                null
        );
        return new ResponseEntity<>(response, HttpStatus.NO_CONTENT);
    }
}
