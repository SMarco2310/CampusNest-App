package com.example.campus_nest_backend.service;

import com.example.campus_nest_backend.dto.Requests.RoomCreateRequestDto;
import com.example.campus_nest_backend.dto.Requests.RoomUpdateRequestDto;
import com.example.campus_nest_backend.dto.Responses.RoomResponseDto;
import com.example.campus_nest_backend.entity.Hostel;
import com.example.campus_nest_backend.entity.Room;
import com.example.campus_nest_backend.exception.DuplicateRoomNumberException;
import com.example.campus_nest_backend.exception.HostelNotFoundException;
import com.example.campus_nest_backend.exception.RoomNotFoundException;
import com.example.campus_nest_backend.repository.HostelRepository;
import com.example.campus_nest_backend.repository.RoomRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RoomService {

    private final HostelRepository hostelRepository;
    private final RoomRepository roomRepository;

    @Transactional
    public RoomResponseDto createRoom(RoomCreateRequestDto request) {
        validateRoomCreateRequest(request);

        Hostel hostel = hostelRepository.findById(request.getHostelId())
                .orElseThrow(() -> new HostelNotFoundException("Hostel not found with ID: " + request.getHostelId()));

        // Check for duplicate room number within the same hostel
        boolean roomExists = roomRepository.existsByRoomNumberAndHostelId(request.getRoomNumber(), request.getHostelId());
        if (roomExists) {
            throw new DuplicateRoomNumberException("Room number " + request.getRoomNumber() + " already exists in this hostel");
        }

        Room room = mapToRoom(request, hostel);

        roomRepository.save(room);

        // Update hostel total rooms count
        hostel.setTotalRooms(hostel.getTotalRooms() + 1);
        hostel.setAvailableRooms((int) hostel.getRooms().stream().filter(Room::isAvailable).count());

        return mapToRoomResponseDto(room);
    }



    @Transactional
    public RoomResponseDto updateRoom(Long roomId, RoomUpdateRequestDto request) {
        if (roomId == null) throw new IllegalArgumentException("Room ID cannot be null");
        validateRoomUpdateRequest(request);

        Room existingRoom = roomRepository.findById(roomId)
                .orElseThrow(() -> new RoomNotFoundException("Room not found with ID: " + roomId));

        // Update allowed fields (price, description, pictures, amenities, status)
        if (request.getPricePerBed() != null) existingRoom.setPricePerBed(request.getPricePerBed());
        if (request.getDescription() != null) existingRoom.setDescription(request.getDescription());
        if (request.getRoomPictures() != null) existingRoom.setRoomPictures(request.getRoomPictures());
        if (request.getAmenities() != null) existingRoom.setAmenities(request.getAmenities());
        existingRoom.setStatus(request.isStatus());

        Room savedRoom = roomRepository.save(existingRoom);

        // Update hostel available rooms count if status changed
        Hostel hostel = savedRoom.getHostel();
        hostel.setAvailableRooms((int) hostel.getRooms().stream().filter(Room::isAvailable).count());

        return mapToRoomResponseDto(savedRoom);
    }

    public RoomResponseDto getRoomById(Long roomId) {
        Room room = roomRepository.findById(roomId)
                .orElseThrow(() -> new RoomNotFoundException("Room not found with ID: " + roomId));
        return mapToRoomResponseDto(room);
    }

    public List<RoomResponseDto> getRoomsByHostelId(Long hostelId) {
        Hostel hostel = hostelRepository.findById(hostelId)
                .orElseThrow(() -> new HostelNotFoundException("Hostel not found with ID: " + hostelId));

        return hostel.getRooms().stream()
                .map(this::mapToRoomResponseDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public void deleteRoom(Long roomId) {
        Room room = roomRepository.findById(roomId)
                .orElseThrow(() -> new RoomNotFoundException("Room not found with ID: " + roomId));

        if (!room.getOccupants().isEmpty()) {
            throw new IllegalStateException("Cannot delete room with occupants.");
        }

        Hostel hostel = room.getHostel();
        roomRepository.delete(room);

        // Update hostel room counts
        hostel.setTotalRooms(hostel.getTotalRooms() - 1);
        hostel.setAvailableRooms((int) hostel.getRooms().stream().filter(Room::isAvailable).count());
    }

    // Validation helpers
    private void validateRoomCreateRequest(RoomCreateRequestDto request) {
        if (request == null) throw new IllegalArgumentException("Room create request cannot be null");
        if (!StringUtils.hasText(request.getRoomNumber()))
            throw new IllegalArgumentException("Room number is required");
        if (request.getRoomCapacity() == null)
            throw new IllegalArgumentException("Room capacity is required");
        if (request.getCapacity() <= 0)
            throw new IllegalArgumentException("Capacity must be positive");
        if (request.getPricePerBed() == null || request.getPricePerBed().compareTo(BigDecimal.ZERO) < 0)
            throw new IllegalArgumentException("Price per bed cannot be negative");
        if (!StringUtils.hasText(request.getDescription()))
            throw new IllegalArgumentException("Description is required");
        if (request.getFloor() < 0)
            throw new IllegalArgumentException("Floor cannot be negative");
        if (request.getHostelId() == null)
            throw new IllegalArgumentException("Hostel ID is required");
    }

    private void validateRoomUpdateRequest(RoomUpdateRequestDto request) {
        if (request == null) throw new IllegalArgumentException("Room update request cannot be null");
        if (request.getPricePerBed() != null && request.getPricePerBed().compareTo(BigDecimal.ZERO) < 0)
            throw new IllegalArgumentException("Price per bed cannot be negative");
        if (request.getDescription() != null && request.getDescription().length() > 1000)
            throw new IllegalArgumentException("Description cannot exceed 1000 characters");
        // Further validations can be added here as needed
    }

    // Mapping method from entity to DTO
    private RoomResponseDto mapToRoomResponseDto(Room room) {
        RoomResponseDto dto = new RoomResponseDto();
        dto.setId(room.getId());
        dto.setRoomNumber(room.getRoomNumber());
        dto.setRoomCapacity(room.getRoomCapacity());
        dto.setCapacity(room.getCapacity());
        dto.setCurrentOccupancy(room.getCurrentOccupancy());
        dto.setPricePerBed(room.getPricePerBed());
        dto.setDescription(room.getDescription());
        dto.setStatus(room.isStatus());
        dto.setRoomPictures(room.getRoomPictures());
        dto.setAmenities(room.getAmenities());
        dto.setFloor(room.getFloor());
        dto.setHasAvailableSpace(room.hasAvailableSpace());
        return dto;
    }

    private static Room mapToRoom(RoomCreateRequestDto request, Hostel hostel) {
        Room room = new Room();
        room.setRoomNumber(request.getRoomNumber());
        room.setRoomCapacity(request.getRoomCapacity());
        room.setCapacity(request.getCapacity());
        room.setPricePerBed(request.getPricePerBed());
        room.setDescription(request.getDescription());
        room.setRoomPictures(request.getRoomPictures());
        room.setAmenities(request.getAmenities());
        room.setFloor(request.getFloor());
        room.setHostel(hostel);
        room.setStatus(true); // default available
        return room;
    }
}
