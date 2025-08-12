package com.example.campus_nest_backend.service;

import com.example.campus_nest_backend.dto.Requests.BookingCancelRequestDto;
import com.example.campus_nest_backend.dto.Requests.BookingCreateRequestDto;
import com.example.campus_nest_backend.dto.Requests.BookingUpdateRequestDto;
import com.example.campus_nest_backend.dto.Responses.*;
import com.example.campus_nest_backend.entity.Booking;
import com.example.campus_nest_backend.entity.Hostel;
import com.example.campus_nest_backend.entity.Room;
import com.example.campus_nest_backend.entity.User;
import com.example.campus_nest_backend.exception.*;
import com.example.campus_nest_backend.repository.BookingRepository;
import com.example.campus_nest_backend.repository.HostelRepository;
import com.example.campus_nest_backend.repository.RoomRepository;
import com.example.campus_nest_backend.repository.UserRepository;
import com.example.campus_nest_backend.utils.Status;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BookingService {

    private final BookingRepository bookingRepository;
    private final RoomRepository roomRepository;
    private final UserRepository userRepository;
    private final HostelRepository hostelRepository;
    private final EmailService emailService;

    /* ------------------- CREATE BOOKING ------------------- */
    @Transactional
    public BookingResponseDto createBooking(BookingCreateRequestDto request) {
        validateCreateRequest(request);

        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new UserNotFoundException("User not found with ID: " + request.getUserId()));

        Room room = roomRepository.findById(request.getRoomId())
                .orElseThrow(() -> new RoomNotFoundException("Room not found with ID: " + request.getRoomId()));

        if (bookingRepository.existsByUserIdAndStatusIn(user.getId(), List.of(Status.PENDING, Status.CONFIRMED))) {
            throw new UserHasActiveBookingException("User already has an active booking");
        }

        if (!isRoomAvailable(room)) {
            throw new RoomUnavailableException("Room is not available or is at full capacity");
        }

        Booking booking = new Booking();
        booking.setUser(user);
        booking.setRoom(room);
        booking.setCheckInDate(request.getCheckInDate());
        booking.setCheckOutDate(request.getCheckOutDate());
        booking.setStatus(Status.PENDING);
        booking.setTotalAmount(room.getPricePerBed());

        updateRoomOccupancy(room, 1);
        updateHostelAvailableRooms(room.getHostel());

        user.setCurrentRoom(room);
        userRepository.save(user);

        Booking savedBooking = bookingRepository.save(booking);
        sendBookingConfirmationEmail(user, room, savedBooking);

        return mapToBookingResponseDto(savedBooking);
    }
    /* -------------------  BOOKINGS ------------------- */
    public List<BookingResponseDto> getAllBookings() {
        return bookingRepository.findAll().stream()
                .map(this::mapToBookingResponseDto)
                .toList();
    }

    public List<BookingResponseDto> getBookingsByRoom(Long roomId) {
        return bookingRepository.findBookingsByRoomId(roomId).stream()
                .map(this::mapToBookingResponseDto)
                .toList();
    }
    public List<BookingResponseDto> getBookingsByHostel(Long hostelId) {
        Hostel hostel = hostelRepository.findById(hostelId)
                .orElseThrow(() -> new HostelNotFoundException("Hostel not found with ID: " + hostelId));

        return Booking.getBookings(hostel).stream()
                .map(this::mapToBookingResponseDto)
                .toList();
    }

    /* ------------------- UPDATE BOOKING ------------------- */
    @Transactional
    public BookingResponseDto updateBooking(Long bookingId, BookingUpdateRequestDto request, Long userId) {
        validateUpdateRequest(request);

        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new BookingNotFoundException("Booking not found with ID: " + bookingId));

        if (!booking.getUser().getId().equals(userId)) {
            throw new IllegalArgumentException("You can only update your own booking");
        }

        if (booking.getStatus() != Status.PENDING) {
            throw new InvalidBookingStatusException("Only pending bookings can be updated");
        }

        booking.setCheckInDate(request.getCheckInDate());
        booking.setCheckOutDate(request.getCheckOutDate());
        booking.setTotalAmount(booking.getRoom().getPricePerBed());

        Booking updatedBooking = bookingRepository.save(booking);
        sendBookingUpdateEmail(booking.getUser(), booking.getRoom(), updatedBooking);

        return mapToBookingResponseDto(updatedBooking);
    }

    /* ------------------- CANCEL BOOKING ------------------- */
    @Transactional
    public void cancelBooking(Long bookingId, Long userId, BookingCancelRequestDto request) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new BookingNotFoundException("Booking not found with ID: " + bookingId));

        if (!booking.getUser().getId().equals(userId)) {
            throw new IllegalArgumentException("You can only cancel your own booking");
        }

        if (!booking.canBeCancelled()) {
            throw new InvalidBookingStatusException("Booking cannot be cancelled");
        }

        booking.cancel();
        booking.setCancellationReason(request.getCancellationReason());

        updateRoomOccupancy(booking.getRoom(), -1);
        updateHostelAvailableRooms(booking.getRoom().getHostel());
        bookingRepository.save(booking);

        sendBookingCancellationEmail(booking.getUser(), booking.getRoom(), booking);
    }

    /* ------------------- GETTERS ------------------- */
    public BookingDetailsResponseDto getBookingDetails(Long bookingId) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new BookingNotFoundException("Booking not found"));

        return mapToBookingDetailsDto(booking);
    }

    public List<BookingSummaryDto> getBookingsByUser(Long userId) {
        return bookingRepository.findBookingsByUserId(userId).stream()
                .map(this::mapToBookingSummaryDto)
                .toList();
    }

    /* ------------------- VALIDATION ------------------- */
    private void validateCreateRequest(BookingCreateRequestDto request) {
        if (request.getCheckInDate() == null || request.getCheckOutDate() == null) {
            throw new IllegalArgumentException("Check-in and check-out dates are required");
        }
        if (!request.getCheckOutDate().isAfter(request.getCheckInDate())) {
            throw new IllegalArgumentException("Check-out date must be after check-in date");
        }
    }

    private void validateUpdateRequest(BookingUpdateRequestDto request) {
        if (request.getCheckInDate() == null || request.getCheckOutDate() == null) {
            throw new IllegalArgumentException("Check-in and check-out dates are required");
        }
        if (!request.getCheckOutDate().isAfter(request.getCheckInDate())) {
            throw new IllegalArgumentException("Check-out date must be after check-in date");
        }
    }

    /* ------------------- UTILITIES ------------------- */
    private boolean isRoomAvailable(Room room) {
        return room.isAvailable() && room.getCurrentOccupancy() < room.getCapacity();
    }

    private void updateRoomOccupancy(Room room, int change) {
        room.setCurrentOccupancy(Math.max(0, room.getCurrentOccupancy() + change));
        room.setAvailable(room.getCurrentOccupancy() < room.getCapacity());
        roomRepository.save(room);
    }

    private void updateHostelAvailableRooms(Hostel hostel) {
        long available = hostel.getRooms().stream().filter(Room::isAvailable).count();
        hostel.setAvailableRooms((int) available);
        hostelRepository.save(hostel);
    }


    /* ------------------- MAPPERS ------------------- */
    private BookingResponseDto mapToBookingResponseDto(Booking booking) {
        return new BookingResponseDto(
                booking.getId(),
                booking.getBookingDate(),
                booking.getCheckInDate(),
                booking.getCheckOutDate(),
                booking.getStatus(),
                booking.getTotalAmount(),
                BigDecimal.ZERO, // amountPaid
                booking.getTotalAmount() // remainingAmount
        );
    }

    private BookingDetailsResponseDto mapToBookingDetailsDto(Booking booking) {
        BookingDetailsResponseDto dto = new BookingDetailsResponseDto();
        dto.setId(booking.getId());
        dto.setBookingDate(booking.getBookingDate());
        dto.setCheckInDate(booking.getCheckInDate());
        dto.setCheckOutDate(booking.getCheckOutDate());
        dto.setDurationMonths((int) (booking.getDurationInDays() / 30));
        dto.setStatus(booking.getStatus());
        dto.setTotalAmount(booking.getTotalAmount());
        dto.setRemainingAmount(booking.getTotalAmount());
        dto.setUser(mapToUserSummary(booking.getUser()));
        dto.setRoom(mapToRoomWithHostelResponseDto(booking.getRoom()));
        dto.setCanBeCancelled(booking.canBeCancelled());
        dto.setActive(booking.isActive());
        return dto;
    }
    private UserSummaryDto mapToUserSummary(User user) {
        return new UserSummaryDto(user.getId(), user.getName(), user.getEmail(),
                user.getProfilePicture());
    }
    private RoomWithHostelResponseDto mapToRoomWithHostelResponseDto(Room room) {
        RoomWithHostelResponseDto dto = new RoomWithHostelResponseDto();
        dto.setId(room.getId());
        dto.setRoomNumber(room.getRoomNumber());
        dto.setRoomCapacity(room.getRoomCapacity());
        dto.setCapacity(room.getCapacity());
        dto.setCurrentOccupancy(room.getCurrentOccupancy());
        dto.setPricePerBed(room.getPricePerBed());
        dto.setDescription(room.getDescription());
        dto.setStatus(room.isAvailable());
        dto.setRoomPictures(room.getRoomPictures());
        dto.setAmenities(room.getAmenities());
        dto.setFloor(room.getFloor());
        dto.setHasAvailableSpace(isRoomAvailable(room));
        dto.setHostel(mapToHostelSummaryDto(room.getHostel()));
        return dto;
    }

    private HostelSummaryDto mapToHostelSummaryDto(Hostel hostel)
    {
        HostelSummaryDto dto = new HostelSummaryDto();
        dto.setId(hostel.getId());
        dto.setHostelName(hostel.getHostelName());
        dto.setLocation(hostel.getLocation());
        dto.setAvailableRooms(hostel.getAvailableRooms());
        return dto;
    }

    private BookingSummaryDto mapToBookingSummaryDto(Booking booking) {
        BookingSummaryDto dto = new BookingSummaryDto();
        dto.setId(booking.getId());
        dto.setCheckInDate(booking.getCheckInDate());
        dto.setCheckOutDate(booking.getCheckOutDate());
        dto.setStatus(booking.getStatus());
        dto.setTotalAmount(booking.getTotalAmount());
        dto.setRoomNumber(booking.getRoom().getRoomNumber());
        dto.setHostelName(booking.getRoom().getHostel().getHostelName());
        return dto;
    }

    /* ------------------- EMAILS ------------------- */
    private void sendBookingConfirmationEmail(User user, Room room, Booking booking) {
        emailService.sendSimpleEmail(user.getEmail(),
                "Booking Confirmation - " + room.getHostel().getHostelName(),
                String.format("Dear %s,\n\nYour booking is confirmed for Room %s in %s.\nBooking ID: %d",
                        user.getName(), room.getRoomNumber(), room.getHostel().getHostelName(), booking.getId()));
    }

    private void sendBookingUpdateEmail(User user, Room room, Booking booking) {
        emailService.sendSimpleEmail(user.getEmail(),
                "Booking Updated - " + room.getHostel().getHostelName(),
                String.format("Dear %s,\n\nYour booking has been updated.\nRoom: %s",
                        user.getName(), room.getRoomNumber()));
    }

    private void sendBookingCancellationEmail(User user, Room room, Booking booking) {
        emailService.sendSimpleEmail(user.getEmail(),
                "Booking Cancelled - " + room.getHostel().getHostelName(),
                String.format("Dear %s,\n\nYour booking has been cancelled.\nReason: %s",
                        user.getName(), booking.getCancellationReason()));
    }
}
