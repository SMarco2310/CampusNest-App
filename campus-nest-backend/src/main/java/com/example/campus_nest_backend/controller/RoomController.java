package com.example.campus_nest_backend.controller;

import com.example.campus_nest_backend.dto.NewRoomRequest;
import com.example.campus_nest_backend.entity.Room;
import com.example.campus_nest_backend.service.RoomService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/rooms")
public class RoomController {

    private final RoomService roomService;

    public RoomController(RoomService roomService) {
        this.roomService = roomService;
    }

    @PostMapping("/add")
    public ResponseEntity<?> addRoom(@RequestBody NewRoomRequest newRoomRequest) {
        return ResponseEntity.ok(Map.of("message","Room added successfully","room", roomService.createRoom(newRoomRequest)));
    }

    @GetMapping("/{hostelId}")
    public ResponseEntity<?> getRooms(@PathVariable String hostelId) {
        return ResponseEntity.ok(Map.of("rooms",roomService.getRoomsByHostelId(Long.parseLong(hostelId))));
    }

    @GetMapping("/details/{roomId}")
    public ResponseEntity<?> getRoomDetails(@PathVariable String roomId) {
        return ResponseEntity.ok(Map.of("room",roomService.getRoomById(Long.parseLong(roomId))));
    }

    @PutMapping("/update/room/{roomId}")
    public ResponseEntity<?> updateRoom(@PathVariable String roomId, NewRoomRequest newRoomRequest ) {
        return ResponseEntity.ok(Map.of("room",roomService.updateRoom(Long.parseLong(roomId), newRoomRequest)));
    }

    @DeleteMapping("/delete/room/{roomId}")
    public ResponseEntity<?> deleteRoom(@PathVariable String roomId) {
        roomService.deleteRoom(Long.parseLong(roomId));
        return ResponseEntity.ok(Map.of("message","Room deleted successfully")); // Return 204 No Content after deletion
    }

}
