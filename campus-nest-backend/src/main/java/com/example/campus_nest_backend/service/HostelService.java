package com.example.campus_nest_backend.service;

import com.example.campus_nest_backend.dto.Requests.HostelRequest;
import com.example.campus_nest_backend.dto.Responses.HostelResponse;
import com.example.campus_nest_backend.entity.Hostel;
import com.example.campus_nest_backend.entity.User;
import com.example.campus_nest_backend.exception.HostelNotFoundException;
import com.example.campus_nest_backend.exception.ManagerNotFoundException;
import com.example.campus_nest_backend.repository.HostelRepository;
import com.example.campus_nest_backend.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import com.example.campus_nest_backend.utils.Role;

import java.util.List;

@Service
@AllArgsConstructor
public class HostelService {

    private final HostelRepository hostelRepository;
    private final  UserRepository userRepository;

    @Transactional
    // This method retrieves all hostels from the database.
    public HostelResponse createHostel(HostelRequest hostelRequest) {
        // Convert HostelRequest to Hostel entity
        User manager = userRepository.findById(hostelRequest.getManagerId())
                .map(user -> {
                    if (user.getRole() != Role.HOSTEL_MANAGER) {
                        throw new ManagerNotFoundException("User is not a manager.");
                    }
                    return user;
                })
                .orElseThrow(() -> new ManagerNotFoundException("Manager not found."));
        Hostel hostel = new Hostel();
        hostel.setName(hostelRequest.getName());
        hostel.setAddress(hostelRequest.getAddress());
        hostel.setDescription(hostelRequest.getDescription());
        hostel.setManager(manager);
        hostelRepository.save(hostel);
        return new HostelResponse(
                hostel.getId(),
                hostel.getName(),
                hostel.getAddress(),
                hostel.getDescription(),
                hostel.getAvailableRooms(),
                hostel.getTotalRooms(),
                manager.getId(),
                hostel.getImageUrls());
    }
    // This method retrieves all hostels from the database.
    public List<HostelResponse> getAllHostels() {
        return hostelRepository.findAll().stream().map( hostel -> new HostelResponse(
                hostel.getId(),
                hostel.getName(),
                hostel.getAddress(),
                hostel.getDescription(),
                hostel.getAvailableRooms(),
                hostel.getTotalRooms(),
                hostel.getManager().getId(),
                hostel.getImageUrls()))
                .toList();
    }

    @Transactional
    // This method retrieves all hostels from the database.
    public HostelResponse updateHostel(Long id,HostelRequest hostelRequest) {
        // Update an existing hostel
        Hostel hostel = hostelRepository.getHostelById(id);

        User manager = userRepository.findById(hostelRequest.getManagerId())
                .map(user -> {
                    if (user.getRole() != Role.HOSTEL_MANAGER) {
                        throw new ManagerNotFoundException("User is not a manager.");
                    }
                    return user;
                })
                .orElseThrow(() -> new ManagerNotFoundException("Manager not found."));

        hostel.setName(hostelRequest.getName());
        hostel.setAddress(hostelRequest.getAddress());
        hostel.setDescription(hostelRequest.getDescription());
        hostel.setManager(manager);

        hostelRepository.save(hostel);
        return new HostelResponse(
                hostel.getId(),
                hostel.getName(),
                hostel.getAddress(),
                hostel.getDescription(),
                hostel.getAvailableRooms(),
                hostel.getTotalRooms(),
                manager.getId(),
                hostel.getImageUrls());
    }

    // This method retrieves a hostel by its ID.
    public HostelResponse getHostelById(Long id) {
        // Retrieve a hostel by its ID
        return hostelRepository.findById(id).map( hostel -> new HostelResponse(
                hostel.getId(),
                hostel.getName(),
                hostel.getAddress(),
                hostel.getDescription(),
                hostel.getAvailableRooms(),
                hostel.getTotalRooms(),
                hostel.getManager().getId(),
                hostel.getImageUrls()))
                .orElseThrow(() -> new HostelNotFoundException("Hostel not found with ID: " + id));
    }
    @Transactional
    // This method deletes a hostel by its ID.
    public void deleteHostel(Long id) {
        // Delete a hostel by its ID
        if (!hostelRepository.existsById(id)) {
            throw new HostelNotFoundException("Hostel not found with ID: " + id);
        }
        hostelRepository.deleteById(id);
    }
}
