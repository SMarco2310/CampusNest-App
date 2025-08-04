package com.example.campus_nest_backend.service;

import com.example.campus_nest_backend.dto.Requests.NewRoomRequest;
import com.example.campus_nest_backend.dto.Responses.RoomResponse;
import com.example.campus_nest_backend.dto.Responses.UserResponse;
import com.example.campus_nest_backend.entity.Hostel;
import com.example.campus_nest_backend.entity.Room;
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
    public RoomResponse createRoom(NewRoomRequest newRoomRequest) {
        Hostel hostel = hostelRepository.findById(newRoomRequest.getHostelId())
                .orElseThrow(() -> new HostelNotFoundException("Hostel not found with ID: " + newRoomRequest.getHostelId()));
        hostel.setTotalRooms(hostel.getTotalRooms() + 1);

        Room room = new Room();
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

        hostel.setAvailableRooms((int) hostel.getRooms().stream().filter(Room::isAvailable).count());

        return new RoomResponse(
                room.getId(),
                room.getRoomNumber(),
                room.getRoomCapacity(),
                room.getCurrentOccupancy(),
                room.getPricePerBed(),
                room.getDescription(),
                hostel.getId(),
                room.isAvailable()
        );
    }

    public List<UserResponse> getUsersInRoom(Long roomId) {
        Room room = roomRepository.findById(roomId)
                .orElseThrow(() -> new RoomNotFoundException("Room with the id " + roomId + " not found."));
        return room.getOccupants().stream().map(
                user -> new UserResponse(
                        user.getId(),
                        user.getName(),
                        user.getEmail(),
                        user.getPhone(),
                        user.getRole(),
                        user.getProfilePicture()
                )
        ).toList(
        );
    }

    @Transactional
    public RoomResponse updateRoom(Long roomId, NewRoomRequest newRoomRequest) {
        Room existingRoom = roomRepository.findById(roomId)
                .orElseThrow(() -> new RoomNotFoundException("Room not found with ID: " + roomId));

        Hostel hostel = hostelRepository.findById(newRoomRequest.getHostelId())
                .orElseThrow(() -> new HostelNotFoundException("Hostel not found with ID: " + newRoomRequest.getHostelId()));

        existingRoom.setRoomNumber(newRoomRequest.getRoomNumber());
        existingRoom.setRoomCapacity(Capacity.fromValue(newRoomRequest.getCapacity()));
        existingRoom.setCapacity(newRoomRequest.getCapacity());
        existingRoom.setFloor(newRoomRequest.getFloor());
        existingRoom.setHostel(hostel);
        existingRoom.setPricePerBed(newRoomRequest.getPricePerBed());
        existingRoom.setDescription(newRoomRequest.getDescription());
        existingRoom.setAmenities(newRoomRequest.getAmenities());
        existingRoom.setImageUrls(newRoomRequest.getImages());

        roomRepository.save(existingRoom);

        return new RoomResponse(
                existingRoom.getId(),
                existingRoom.getRoomNumber(),
                existingRoom.getRoomCapacity(),
                existingRoom.getCurrentOccupancy(),
                existingRoom.getPricePerBed(),
                existingRoom.getDescription(),
                hostel.getId(),
                existingRoom.isAvailable()
        );
    }

    public RoomResponse getRoomById(Long roomId) {
        Room room = roomRepository.findById(roomId)
                .orElseThrow(() -> new RoomNotFoundException("Room not found with ID: " + roomId));

        return new RoomResponse(
                room.getId(),
                room.getRoomNumber(),
                room.getRoomCapacity(),
                room.getCurrentOccupancy(),
                room.getPricePerBed(),
                room.getDescription(),
                room.getHostel().getId(),
                room.isAvailable()
        );
    }

    public List<Room> getRoomsByHostelId(Long hostelId) {
        Hostel hostel = hostelRepository.findById(hostelId)
                .orElseThrow(() -> new HostelNotFoundException("Hostel not found with ID: " + hostelId));

        return hostel.getRooms();
    }

    @Transactional
    public void deleteRoom(Long roomId) {
        Room room = roomRepository.findById(roomId)
                .orElseThrow(() -> new RoomNotFoundException("Room not found with ID: " + roomId));

        if (!room.getOccupants().isEmpty()) {
            throw new RuntimeException("Cannot delete room with occupants.");
        }

        Hostel hostel = room.getHostel();
        hostel.setTotalRooms(hostel.getTotalRooms() - 1);
        roomRepository.deleteById(roomId);

        hostel.setAvailableRooms((int) hostel.getRooms().stream().filter(Room::isAvailable).count());
    }

    public void save(Room room) {
        roomRepository.save(room);
    }
}
