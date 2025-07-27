package com.example.campus_nest_backend.service;

import com.example.campus_nest_backend.dto.HostelRequest;
import com.example.campus_nest_backend.entity.Hostel;
import com.example.campus_nest_backend.entity.User;
import com.example.campus_nest_backend.repository.HostelRepository;
import com.example.campus_nest_backend.repository.UserRepository;
import org.springframework.stereotype.Service;
import com.example.campus_nest_backend.utils.Role;

import java.util.List;

@Service
public class HostelService {

    private final HostelRepository hostelRepository;
    private final  UserRepository userRepository;

    public HostelService(HostelRepository hostelRepository, UserRepository userRepository) {
        this.hostelRepository = hostelRepository;
        this.userRepository = userRepository;
    }


    //  This method creates a new hostel in the database.
    public Hostel createHostel(HostelRequest hostelRequest) {
        // Convert HostelRequest to Hostel entity
        User manager = userRepository.findById(hostelRequest.getManagerId())
                .filter(user -> user.getRole() == Role.HOSTEL_MANAGER)
                .orElseThrow(() -> new IllegalArgumentException("Selected user is not a manager or doesn't exist."));
        Hostel hostel = new Hostel();
        hostel.setName(hostelRequest.getName());
        hostel.setAddress(hostelRequest.getAddress());
        hostel.setDescription(hostelRequest.getDescription());
        hostel.setManager(manager);
        return hostelRepository.save(hostel);
    }
    // This method creates a new hostel in the database.
    public List<Hostel> getAllHostels() {
        return hostelRepository.findAll();
    }

    // This method retrieves all hostels from the database.
    public Hostel updateHostel(Long id,HostelRequest hostelRequest) {
        // Update an existing hostel
        Hostel hostel = hostelRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Hostel not found with ID: " + id));

        User manager = userRepository.findById(hostelRequest.getManagerId())
                .filter(user -> user.getRole() == Role.HOSTEL_MANAGER)
                .orElseThrow(() -> new IllegalArgumentException("Selected user is not a manager or doesn't exist."));

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
                .orElseThrow(() -> new IllegalArgumentException("Hostel not found with ID: " + id));
    }

    // This method deletes a hostel by its ID.
    public void deleteHostel(Long id) {
        // Delete a hostel by its ID
        if (!hostelRepository.existsById(id)) {
            throw new IllegalArgumentException("Hostel not found with ID: " + id);
        }
        hostelRepository.deleteById(id);
    }
}
