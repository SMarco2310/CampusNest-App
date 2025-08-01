package com.example.campus_nest_backend.service;

import com.example.campus_nest_backend.dto.NewRoomRequest;
import com.example.campus_nest_backend.entity.Hostel;
import com.example.campus_nest_backend.entity.Room;
import com.example.campus_nest_backend.entity.User;
import com.example.campus_nest_backend.exception.HostelNotFoundException;
import com.example.campus_nest_backend.exception.RoomNotFoundException;
import com.example.campus_nest_backend.repository.HostelRepository;
import com.example.campus_nest_backend.repository.RoomRepository;
import com.example.campus_nest_backend.utils.Capacity;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@RequiredArgsConstructor
public class RoomService {

    private final HostelRepository hostelRepository;
    private final RoomRepository roomRepository;
    @Transactional
    public Room createRoom(NewRoomRequest newRoomRequest) {
        Hostel hostel = hostelRepository.findById(newRoomRequest.getHostelId())
                .orElseThrow(() -> new HostelNotFoundException("Hostel not found with ID: " + newRoomRequest.getHostelId()));
        hostel.setTotalRooms(hostel.getTotalRooms() + 1);
        Room room = new Room();
        hostel.setAvailableRooms(
                (int) hostel.getRooms().stream()
                        .filter(Room::isAvailable)
                        .count()
        );
        room.setRoomNumber(newRoomRequest.getRoomNumber());
        room.setRoomCapacity(Capacity.fromValue(newRoomRequest.getCapacity()));
        room.setCapacity(newRoomRequest.getCapacity());
        room.setFloor(newRoomRequest.getFloor());

        room.setHostel(hostel);
        room.setPricePerBed(newRoomRequest.getPricePerBed());
        room.setDescription(newRoomRequest.getDescription());
        room.setAmenities(newRoomRequest.getAmenities());
        room.setImageUrls(newRoomRequest.getImages());

        roomRepository.save(room);
        return room;
    }


    public List<User> getUsersInRoom(Long roomId) {
        Room room = roomRepository.findById(roomId).orElseThrow(() -> new RoomNotFoundException("Room with the id " +roomId + "Not found."));
        return room.getOccupants();
    }

    @Transactional
    public Room updateRoom(Long roomId, NewRoomRequest newRoomRequest) {
        // Fetch the existing room by ID
        Room existingRoom = roomRepository.findById(roomId)
                .orElseThrow(() -> new RuntimeException("Room not found with ID: " + roomId));

        Hostel hostel = hostelRepository.findById(newRoomRequest.getHostelId())
                .orElseThrow(() -> new HostelNotFoundException("Hostel not found with ID: " + newRoomRequest.getHostelId()));
        existingRoom.setHostel(hostel);
        // Update the room properties
        existingRoom.setRoomNumber(newRoomRequest.getRoomNumber());
        existingRoom.setRoomCapacity(Capacity.fromValue(newRoomRequest.getCapacity()));
        existingRoom.setCapacity(newRoomRequest.getCapacity());
        existingRoom.setFloor(newRoomRequest.getFloor());
        existingRoom.setHostel(hostel);
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
        Hostel hostel = hostelRepository.findById(hostelId)
                .orElseThrow(() -> new HostelNotFoundException("Hostel not found with ID: " + hostelId));

        return hostel.getRooms();
    }
    @Transactional
    public void deleteRoom(Long roomId) {
        // Delete the room by ID from the repository
        Room room = roomRepository.findById(roomId)
                .orElseThrow(() -> new RoomNotFoundException("Room not found with ID: " + roomId));
        if (!room.getOccupants().isEmpty()) {
            throw new RuntimeException("Cannot delete room with occupants.");
        }
        Hostel hostel = room.getHostel();
        hostel.setAvailableRooms(
                (int) hostel.getRooms().stream()
                        .filter(Room::isAvailable)
                        .count()
        );
        hostel.setTotalRooms(hostel.getTotalRooms() - 1);
        roomRepository.deleteById(roomId);
    }

    public void save(Room room) {
        roomRepository.save(room);
    }


//    I could use this to sync the room counts in hostels periodically, but it is commented out for now.


//    @Scheduled(cron = "0 0 * * * ?") // every hour
//public void syncRoomCounts() {
//    hostelRepository.findAll().forEach(hostel -> {
//        int actualRoomCount = hostel.getRooms().size();
//        hostel.setTotalRooms(actualRoomCount);
//        hostelRepository.save(hostel);
//    });
//}
}
