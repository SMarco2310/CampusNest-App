package com.example.campus_nest_backend.service;

import com.example.campus_nest_backend.dto.NewRoomRequest;
import com.example.campus_nest_backend.entity.Hostel;
import com.example.campus_nest_backend.entity.Room;
import com.example.campus_nest_backend.repository.HostelRepository;
import com.example.campus_nest_backend.repository.RoomRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoomService {

    private final HostelRepository hostelRepository;
    private final RoomRepository roomRepository;

    public RoomService(HostelRepository hostelRepository, RoomRepository roomRepository) {
        this.hostelRepository = hostelRepository;
        this.roomRepository = roomRepository;
    }

    public void createRoom(NewRoomRequest newRoomRequest) {
        Room room = new Room();
        room.setRoomNumber(newRoomRequest.getRoomNumber());
        room.setCapacity(newRoomRequest.getCapacity());
        room.setHostel(hostelRepository.getHostelById(newRoomRequest.getHostelId()));
        if (room.getHostel() == null) {
            throw new RuntimeException("Hostel not found with ID: " + newRoomRequest.getHostelId());
        }
        // Set other room properties from the request
        room.setPricePerBed(newRoomRequest.getPricePerBed());
        room.setDescription(newRoomRequest.getDescription());
        room.setAmenities(newRoomRequest.getAmenities());
        room.setImageUrls(newRoomRequest.getImages());
        // Save the room to the database
        roomRepository.save(room);
    }

    public Room updateRoom(Long roomId, NewRoomRequest newRoomRequest) {
        // Fetch the existing room by ID
        Room existingRoom = roomRepository.findById(roomId)
                .orElseThrow(() -> new RuntimeException("Room not found with ID: " + roomId));

        // Update the room properties
        existingRoom.setRoomNumber(newRoomRequest.getRoomNumber());
        existingRoom.setCapacity(newRoomRequest.getCapacity());
        existingRoom.setHostel(hostelRepository.getHostelById(newRoomRequest.getHostelId()));
        if (existingRoom.getHostel() == null) {
            throw new RuntimeException("Hostel not found with ID: " + newRoomRequest.getHostelId());
        }
        existingRoom.setPricePerBed(newRoomRequest.getPricePerBed());
        existingRoom.setDescription(newRoomRequest.getDescription());
        existingRoom.setAmenities(newRoomRequest.getAmenities());
        existingRoom.setImageUrls(newRoomRequest.getImages());

        // Save the updated room to the database
        return roomRepository.save(existingRoom);
    }

    public Room getRoomById(Long roomId) {
        // Fetch the room by ID from the repository
        return roomRepository.findById(roomId)
                .orElseThrow(() -> new RuntimeException("Room not found with ID: " + roomId));
    }

    // Example method to fetch rooms by hostel ID
    public List<Room> getRoomsByHostelId(Long hostelId) {
        List<Room> rooms =hostelRepository.findById(hostelId)
                .map(Hostel::getRooms).orElse(null);
        if(rooms==null){
            throw new RuntimeException("Hostel not found with ID: " + hostelId);
        }
        return rooms;
    }

    public void deleteRoom(Long roomId) {
        // Delete the room by ID from the repository
        roomRepository.deleteById(roomId);
    }


}
