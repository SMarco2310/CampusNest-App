package com.example.campus_nest_backend.controller;

import com.example.campus_nest_backend.dto.Requests.NewRoomRequest;
import com.example.campus_nest_backend.service.RoomService;
import org.springframework.http.ResponseEntity;
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

    @GetMapping("/rooms/{hostelId}")
    public ResponseEntity<?> getRooms(@PathVariable
                                      Long hostelId) {
        return ResponseEntity.ok(Map.of("rooms",roomService.getRoomsByHostelId(hostelId)));
    }

    @GetMapping("/details/{roomId}")
    public ResponseEntity<?> getRoomDetails(@PathVariable Long roomId) {
        return ResponseEntity.ok(Map.of("room",roomService.getRoomById(roomId)));
    }

    @PutMapping("/update/room/{roomId}")
    public ResponseEntity<?> updateRoom(@PathVariable Long roomId, @RequestBody NewRoomRequest newRoomRequest ) {
        return ResponseEntity.ok(Map.of("room",roomService.updateRoom(roomId, newRoomRequest)));
    }

    @DeleteMapping("/delete/room/{roomId}")
    public ResponseEntity<?> deleteRoom(@PathVariable Long roomId) {
        roomService.deleteRoom(roomId);
        return ResponseEntity.ok(Map.of("message","Room deleted successfully")); // Return 204 No Content after deletion
    }

    @GetMapping("/occupant/room/{roomId}")
    public ResponseEntity<?> getOccupantRooms(@PathVariable Long roomId) {
        return ResponseEntity.ok(Map.of("occupants",roomService.getUsersInRoom(roomId)));
    }
}
