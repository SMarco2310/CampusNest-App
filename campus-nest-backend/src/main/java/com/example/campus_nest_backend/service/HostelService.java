package com.example.campus_nest_backend.service;

import com.example.campus_nest_backend.dto.HostelRequest;
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
    public Hostel createHostel(HostelRequest hostelRequest) {
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
        return hostelRepository.save(hostel);
    }
    // This method retrieves all hostels from the database.
    public List<Hostel> getAllHostels() {
        return hostelRepository.findAll();
    }

    @Transactional
    // This method retrieves all hostels from the database.
    public Hostel updateHostel(Long id,HostelRequest hostelRequest) {
        // Update an existing hostel
        Hostel hostel = hostelRepository.findById(id)
                .orElseThrow(() -> new HostelNotFoundException("Hostel not found with ID: " + id));

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

        return hostelRepository.save(hostel);
    }

    // This method retrieves a hostel by its ID.
    public Hostel getHostelById(Long id) {
        // Retrieve a hostel by its ID
        return hostelRepository.findById(id)
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
