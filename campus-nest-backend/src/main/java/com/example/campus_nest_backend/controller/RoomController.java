package com.example.campus_nest_backend.controller;

import com.example.campus_nest_backend.dto.NewRoomRequest;
import com.example.campus_nest_backend.entity.Room;
import com.example.campus_nest_backend.service.RoomService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller("api/rooms")
public class RoomController {

    private final RoomService roomService;

    public RoomController(RoomService roomService) {
        this.roomService = roomService;
    }

    @PostMapping("/add")
    public ResponseEntity<?> addRoom(NewRoomRequest newRoomRequest) {
        roomService.createRoom(newRoomRequest);
        return ResponseEntity.ok("Room added successfully");
    }

    @GetMapping("/{hostelId}")
    public ResponseEntity<List<Room>> getRooms(@PathVariable String hostelId) {
        return ResponseEntity.ok(roomService.getRoomsByHostelId(Long.parseLong(hostelId)));
    }

    @GetMapping("/details/{roomId}")
    public ResponseEntity<Room> getRoomDetails(@PathVariable String roomId) {
        return ResponseEntity.ok(roomService.getRoomById(Long.parseLong(roomId)));
    }

    @PutMapping("/update/room/{roomId}")
    public ResponseEntity<Room> updateRoom(@PathVariable String roomId, NewRoomRequest newRoomRequest ) {
        return ResponseEntity.ok(roomService.updateRoom(Long.parseLong(roomId), newRoomRequest));
    }

    @DeleteMapping("/delete/room/{roomId}")
    public ResponseEntity<?> deleteRoom(@PathVariable String roomId) {
        roomService.deleteRoom(Long.parseLong(roomId));
        return ResponseEntity.ok("Room deleted successfully"); // Return 204 No Content after deletion
    }

}
