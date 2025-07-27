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
    @GetMapping("{id}/details")
    public ResponseEntity<Room> getRoomDetails(@PathVariable String id) {
        return ResponseEntity.ok(roomService.getRoomById(Long.parseLong(id)));
    }

    @PutMapping("/update/room/{id}")
    public ResponseEntity<Room> updateRoom(@PathVariable String id, NewRoomRequest newRoomRequest ) {
        return ResponseEntity.ok(roomService.updateRoom(Long.parseLong(id), newRoomRequest));
    }

    @DeleteMapping("/delete/room/{id}")
    public ResponseEntity<?> deleteRoom(@PathVariable String id) {
        roomService.deleteRoom(Long.parseLong(id));
        return ResponseEntity.ok("Room deleted successfully"); // Return 204 No Content after deletion
    }

}
