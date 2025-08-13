package com.example.campus_nest_backend.service;

import com.example.campus_nest_backend.dto.Requests.HostelCreateRequestDto;
import com.example.campus_nest_backend.dto.Requests.HostelUpdateRequestDto;
import com.example.campus_nest_backend.dto.Responses.*;
import com.example.campus_nest_backend.entity.*;
import com.example.campus_nest_backend.exception.DuplicateHostelNameException;
import com.example.campus_nest_backend.exception.HostelNotFoundException;
import com.example.campus_nest_backend.exception.ManagerNotFoundException;
import com.example.campus_nest_backend.repository.HostelRepository;
import com.example.campus_nest_backend.repository.UserRepository;
import com.example.campus_nest_backend.utils.Role;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class HostelService {

    private final HostelRepository hostelRepository;
    private final UserRepository userRepository;

    public HostelService(HostelRepository hostelRepository, UserRepository userRepository) {
        this.hostelRepository = hostelRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public HostelResponseDto createHostel(HostelCreateRequestDto request) {
        validateHostelCreateRequest(request);

        if (hostelRepository.existsByHostelNameIgnoreCase(request.getHostelName())) {
            throw new DuplicateHostelNameException("Hostel with name '" + request.getHostelName() + "' already exists");
        }

        Hostel_Manager manager = findAndValidateManager(request.getManagerId());

        if (hostelRepository.existsByManagerId(manager.getId())) {
            throw new ManagerNotFoundException("Manager is already assigned to another hostel");
        }

        Hostel hostel = new Hostel();
        hostel.setHostelName(request.getHostelName());
        hostel.setLocation(request.getLocation());
        hostel.setDescription(request.getDescription());
        hostel.setHostelPictures(request.getHostelPictures());
        hostel.setManager(manager);
        hostel.setCheckInTime(request.getCheckInTime());
        hostel.setCheckOutTime(request.getCheckOutTime());

        List<BankAccountDetails> bankAccounts = manager.getBankAccountDetails().stream()
                .map(dto -> {
                    BankAccountDetails details = new BankAccountDetails();
                    details.setAccountName(dto.getAccountName());
                    details.setBankName(dto.getBankName());
                    details.setCurrency(dto.getCurrency());
                    details.setAccountNumber(dto.getAccountNumber());
                    return details;
                })
                .collect(Collectors.toList());

        hostel.setBankAccountDetails(bankAccounts);

        Hostel savedHostel = hostelRepository.save(hostel);
        return mapToHostelResponseDto(savedHostel);
    }

    @Transactional
    public HostelResponseDto updateHostel(Long id, HostelUpdateRequestDto request) {
        if (id == null) {
            throw new IllegalArgumentException("Hostel ID cannot be null");
        }
        validateHostelUpdateRequest(request);

        Hostel existingHostel = hostelRepository.findById(id)
                .orElseThrow(() -> new HostelNotFoundException("Hostel not found with ID: " + id));

        if (StringUtils.hasText(request.getHostelName()) &&
                !existingHostel.getHostelName().equalsIgnoreCase(request.getHostelName()) &&
                hostelRepository.existsByHostelNameIgnoreCaseAndIdNot(request.getHostelName(), id)) {
            throw new DuplicateHostelNameException("Hostel with name '" + request.getHostelName() + "' already exists");
        }

        if (StringUtils.hasText(request.getHostelName())) existingHostel.setHostelName(request.getHostelName());
        if (StringUtils.hasText(request.getLocation())) existingHostel.setLocation(request.getLocation());
        if (StringUtils.hasText(request.getDescription())) existingHostel.setDescription(request.getDescription());
        if (request.getHostelPictures() != null) existingHostel.setHostelPictures(request.getHostelPictures());

        Hostel updatedHostel = hostelRepository.save(existingHostel);
        return mapToHostelResponseDto(updatedHostel);
    }

    public List<HostelSummaryDto> getAllHostelsSummary() {
        return hostelRepository.findAll()
                .stream()
                .map(this::mapToHostelSummaryDto)
                .collect(Collectors.toList());
    }

    public HostelDetailResponseDto getHostelDetailById(Long id) {
        Hostel hostel = hostelRepository.findById(id)
                .orElseThrow(() -> new HostelNotFoundException("Hostel not found with ID: " + id));

        return mapToHostelDetailResponseDto(hostel);
    }

    @Transactional
    public void deleteHostel(Long id) {
        Hostel hostel = hostelRepository.findById(id)
                .orElseThrow(() -> new HostelNotFoundException("Hostel not found with ID: " + id));

        if (hostel.getTotalRooms() > 0) {
            throw new IllegalStateException("Cannot delete hostel with existing rooms. Remove all rooms first.");
        }

        hostelRepository.deleteById(id);
    }

    // ======= PRIVATE VALIDATION METHODS =======

    private void validateHostelCreateRequest(HostelCreateRequestDto request) {
        if (request == null) throw new IllegalArgumentException("Hostel create request cannot be null");

        if (!StringUtils.hasText(request.getHostelName()))
            throw new IllegalArgumentException("Hostel name is required");
        if (request.getHostelName().length() < 2 || request.getHostelName().length() > 100)
            throw new IllegalArgumentException("Hostel name must be between 2 and 100 characters");

        if (!StringUtils.hasText(request.getLocation()))
            throw new IllegalArgumentException("Location is required");
        if (request.getLocation().length() > 500)
            throw new IllegalArgumentException("Location cannot exceed 500 characters");

        if (!StringUtils.hasText(request.getDescription()))
            throw new IllegalArgumentException("Description is required");
        if (request.getDescription().length() > 2000)
            throw new IllegalArgumentException("Description cannot exceed 2000 characters");

        if (request.getManagerId() == null)
            throw new IllegalArgumentException("Manager ID is required");

        if (request.getHostelPictures() != null) {
            for (String url : request.getHostelPictures()) {
                if (url.length() > 2048) // example length check for URL
                    throw new IllegalArgumentException("Hostel picture URLs cannot exceed 2048 characters");
            }
        }
        // Optional: Validate bank account details here similarly if needed
    }

    private void validateHostelUpdateRequest(HostelUpdateRequestDto request) {
        if (request == null) throw new IllegalArgumentException("Hostel update request cannot be null");

        if (request.getHostelName() != null) {
            if (request.getHostelName().length() < 2 || request.getHostelName().length() > 100)
                throw new IllegalArgumentException("Hostel name must be between 2 and 100 characters");
        }

        if (request.getLocation() != null) {
            if (request.getLocation().length() > 500)
                throw new IllegalArgumentException("Location cannot exceed 500 characters");
        }

        if (request.getDescription() != null) {
            if (request.getDescription().length() > 2000)
                throw new IllegalArgumentException("Description cannot exceed 2000 characters");
        }

        if (request.getHostelPictures() != null) {
            for (String url : request.getHostelPictures()) {
                if (url.length() > 2048)
                    throw new IllegalArgumentException("Hostel picture URLs cannot exceed 2048 characters");
            }
        }
    }

    // ======= PRIVATE HELPER METHODS =======

    private Hostel_Manager findAndValidateManager(Long managerId) {
        return userRepository.findById(managerId)
                .filter(user -> user instanceof Hostel_Manager)
                .map(user -> (Hostel_Manager) user)
                .orElseThrow(() -> {
                    if (userRepository.existsById(managerId)) {
                        return new ManagerNotFoundException("User with ID " + managerId + " is not a hostel manager");
                    } else {
                        return new ManagerNotFoundException("Manager not found with ID: " + managerId);
                    }
                });
    }


    private HostelResponseDto mapToHostelResponseDto(Hostel hostel) {
        HostelResponseDto dto = new HostelResponseDto();
        dto.setId(hostel.getId());
        dto.setHostelName(hostel.getHostelName());
        dto.setLocation(hostel.getLocation());
        dto.setDescription(hostel.getDescription());
        dto.setHostelPictures(hostel.getHostelPictures());
        dto.setTotalRooms(hostel.getTotalRooms());
        dto.setAvailableRooms(hostel.getAvailableRooms());
        dto.setCurrentOccupancy(hostel.getCurrentOccupancy());
        dto.setTotalOccupancy(hostel.getTotalOccupancy());
        dto.setMinPrice(hostel.getMinPrice());
        dto.setMaxPrice(hostel.getMaxPrice());
        dto.setAverageRatings(hostel.getAverageRatings());
        dto.setTotalReviews(hostel.getReviews().size());
        dto.setCreatedAt(hostel.getCreatedAt());
        dto.setUpdatedAt(hostel.getUpdatedAt());
        dto.setCheckInTime(hostel.getCheckInTime());
        dto.setCheckOutTime(hostel.getCheckOutTime());
        return dto;
    }

    private HostelSummaryDto mapToHostelSummaryDto(Hostel hostel) {
        HostelSummaryDto dto = new HostelSummaryDto();
        dto.setId(hostel.getId());
        dto.setHostelName(hostel.getHostelName());
        dto.setLocation(hostel.getLocation());
        dto.setHostelPictures(hostel.getHostelPictures());
        dto.setMinPrice(hostel.getMinPrice());
        dto.setAverageRating(hostel.getAverageRatings());
        dto.setAvailableRooms(hostel.getAvailableRooms());
        return dto;
    }

    private HostelDetailResponseDto mapToHostelDetailResponseDto(Hostel hostel) {
        HostelDetailResponseDto dto = new HostelDetailResponseDto();
        dto.setId(hostel.getId());
        dto.setHostelName(hostel.getHostelName());
        dto.setLocation(hostel.getLocation());
        dto.setDescription(hostel.getDescription());
        dto.setHostelPictures(hostel.getHostelPictures());
        dto.setTotalRooms(hostel.getTotalRooms());
        dto.setAvailableRooms(hostel.getAvailableRooms());
        dto.setCurrentOccupancy(hostel.getCurrentOccupancy());
        dto.setTotalOccupancy(hostel.getTotalOccupancy());
        dto.setMinPrice(hostel.getMinPrice());
        dto.setMaxPrice(hostel.getMaxPrice());
        dto.setAverageRatings(hostel.getAverageRatings());
        dto.setTotalReviews(hostel.getReviews().size());
        dto.setCreatedAt(hostel.getCreatedAt());
        dto.setUpdatedAt(hostel.getUpdatedAt());
        dto.setCheckInTime(hostel.getCheckInTime());
        dto.setCheckOutTime(hostel.getCheckOutTime());

        dto.setManager(mapToManagerResponseDto(hostel.getManager()));

        dto.setRooms(hostel.getRooms().stream()
                .map(this::mapToRoomResponseDto)
                .collect(Collectors.toList()));

        dto.setReviews(hostel.getReviews().stream()
                .map(this::mapToReviewResponseDto)
                .collect(Collectors.toList()));

        dto.setBankAccountDetails(hostel.getBankAccountDetails().stream()
                .map(this::mapToBankAccountDetailsResponseDto)
                .collect(Collectors.toList()));

        return dto;
    }

    private ManagerResponseDto mapToManagerResponseDto(User user) {
        ManagerResponseDto dto = new ManagerResponseDto();
        dto.setId(user.getId());
        dto.setName(user.getName());
        dto.setEmail(user.getEmail());
        dto.setProfileImageUrl(user.getProfilePicture());
        return dto;
    }

    private UserSummaryDto mapToUserSummaryDto(User user) {
        UserSummaryDto dto = new UserSummaryDto();
        dto.setId(user.getId());
        dto.setName(user.getName());
        dto.setEmail(user.getEmail());
        dto.setProfilePicture(user.getProfilePicture());
        return dto;
    }
    private RoomResponseDto mapToRoomResponseDto(Room room) {
        if (room == null) return null;
        RoomResponseDto dto = new RoomResponseDto();
        dto.setId(room.getId());
        dto.setRoomNumber(room.getRoomNumber());
        dto.setRoomCapacity(room.getRoomCapacity());
        dto.setPricePerBed(room.getPricePerBed());
        dto.setDescription(room.getDescription());
        dto.setStatus(room.isAvailable());
        dto.setRoomPictures(room.getRoomPictures());
        dto.setAmenities(room.getAmenities());
        dto.setFloor(room.getFloor());
        dto.setHasAvailableSpace(room.hasAvailableSpace());
        dto.setCapacity(room.getCapacity());
        // add more mappings depending on your RoomResponseDto fields
        return dto;
    }


    private ReviewResponseDto mapToReviewResponseDto(Review review) {
        if (review == null) return null;
        ReviewResponseDto dto = new ReviewResponseDto();
        dto.setId(review.getId());
        dto.setRating(review.getRating());
        dto.setComment(review.getComment());
        dto.setCreatedAt(review.getCreatedAt());
        dto.setUser(mapToUserSummaryDto(review.getStudent()));
        // map other fields if needed
        return dto;
    }


    private BankAccountDetailsResponseDto mapToBankAccountDetailsResponseDto(BankAccountDetails bankAccount) {
        BankAccountDetailsResponseDto dto = new BankAccountDetailsResponseDto();
        dto.setAccountName(bankAccount.getAccountName());
        dto.setBankName(bankAccount.getBankName());
        dto.setCurrency(bankAccount.getCurrency());
        dto.setAccountNumber(bankAccount.getAccountNumber());
        return dto;
    }
}
