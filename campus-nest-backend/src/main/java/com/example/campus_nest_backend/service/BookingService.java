package com.example.campus_nest_backend.service;

import com.example.campus_nest_backend.dto.Requests.BookingRequest;
import com.example.campus_nest_backend.dto.Responses.BookingResponse;
import com.example.campus_nest_backend.entity.Booking;
import com.example.campus_nest_backend.entity.Hostel;
import com.example.campus_nest_backend.entity.Room;
import com.example.campus_nest_backend.entity.User;
import com.example.campus_nest_backend.exception.BookingNotFoundException;
import com.example.campus_nest_backend.exception.RoomUnavailableException;
import com.example.campus_nest_backend.exception.UserHasARoomAlreadyException;
import com.example.campus_nest_backend.repository.BookingRepository;
import com.example.campus_nest_backend.repository.RoomRepository;
import com.example.campus_nest_backend.repository.UserRepository;
import com.example.campus_nest_backend.utils.Status;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.List;

@Service
public class BookingService {

    private final BookingRepository bookingRepository;
    private final RoomService roomService;
    private final EmailService emailService;
    private final UserRepository userRepository;
    private final RoomRepository roomRepository;

    public BookingService(BookingRepository bookingRepository, RoomService roomService, EmailService emailService, UserRepository userRepository, RoomRepository roomRepository) {
        this.bookingRepository = bookingRepository;
        this.roomService = roomService;
        this.emailService = emailService;
        this.userRepository = userRepository;
        this.roomRepository = roomRepository;
    }

    @Transactional
    public BookingResponse bookRoom(BookingRequest bookingRequest) {
        Booking booking = new Booking();
        Room room = roomRepository.findRoomById(bookingRequest.getRoomId());
        User user = userRepository.findUserById(bookingRequest.getUserId());
        Hostel hostel = room.getHostel();
        if (bookingRepository.existsByUserId((user.getId()))){
            throw new UserHasARoomAlreadyException("User already has a booking.");
        }

        if (!room.isAvailable() || room.getCurrentOccupancy() >= room.getCapacity()) {
            throw new RoomUnavailableException("Room is not available or is at full capacity");
        }

        room.setCurrentOccupancy(room.getCurrentOccupancy() + 1);
        if (room.getCurrentOccupancy() >= room.getCapacity()) {
            room.setAvailable(false);
        }

        booking.setUser(user);
        booking.setRoom(room);
        booking.setBookingDate(bookingRequest.getBookingDate());

        hostel.setAvailableRooms((int) hostel.getRooms().stream().filter(Room::isAvailable).count());

        emailService.sendSimpleEmail(
                user.getEmail(),
                "Thank You for booking with Us",
                "Thank you for Booking with Us " + hostel.getName()
        );

        user.setRoom(room);
        roomService.save(room);


        booking = bookingRepository.save(booking);

        return new BookingResponse(
                booking.getId(),
                user.getName(),
                room.getRoomNumber(),
                booking.getStatus().name(),
                user.getId(),
                room.getId(),
                new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'").format(booking.getBookingDate())
        );
    }

    public List<Booking> getAllBookings() {
        return bookingRepository.findAll();
    }

    public BookingResponse findBooking(Long id) {
        Booking booking = bookingRepository.findById(id).orElse(null);
        if (booking == null) return null;

        return new BookingResponse(
                booking.getId(),
                booking.getUser().getName(),
                booking.getRoom().getRoomNumber(),
                booking.getStatus().name(),
                booking.getUser().getId(),
                booking.getRoom().getId(),
                new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'").format(booking.getBookingDate())
        );
    }

    public List<Booking> findBookingsByUserId(Long userId) {
        return bookingRepository.findBookingsByUserId(userId);
    }

    @Transactional
    public BookingResponse updateBooking(Long id, BookingRequest bookingRequest) {
        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() -> new BookingNotFoundException("This booking does not exist"));

        Room oldRoom = booking.getRoom();
        Room newRoom = roomRepository.findRoomById(bookingRequest.getRoomId());
        User user = userRepository.findUserById(bookingRequest.getUserId());

        if (!oldRoom.getId().equals(newRoom.getId())) {
            if (oldRoom.getCurrentOccupancy() > 0) {
                oldRoom.setCurrentOccupancy(oldRoom.getCurrentOccupancy() - 1);
                oldRoom.setAvailable(true);
                roomService.save(oldRoom);
            }

            if (!newRoom.isAvailable() || newRoom.getCurrentOccupancy() >= newRoom.getCapacity()) {
                throw new RoomUnavailableException("New room is not available or full");
            }

            newRoom.setCurrentOccupancy(newRoom.getCurrentOccupancy() + 1);
            if (newRoom.getCurrentOccupancy() >= newRoom.getCapacity()) {
                newRoom.setAvailable(false);
            }

            roomService.save(newRoom);
            booking.setRoom(newRoom);
        }

        booking.setUser(user);
        booking.setBookingDate(bookingRequest.getBookingDate());

        emailService.sendSimpleEmail(
                user.getEmail(),
                "Booking Updated",
                "Your Booking has been updated to Room " + newRoom.getRoomNumber()
        );

        booking = bookingRepository.save(booking);

        return new BookingResponse(
                booking.getId(),
                user.getName(),
                newRoom.getRoomNumber(),
                booking.getStatus().name(),
                user.getId(),
                newRoom.getId(),
                new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'").format(booking.getBookingDate())
        );
    }

    @Transactional
    public void deleteBooking(Long id) {
        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() -> new BookingNotFoundException("This booking does not exist"));

        Room room = booking.getRoom();
        User user = booking.getUser();

        if (room.getCurrentOccupancy() > 0) {
            room.setCurrentOccupancy(room.getCurrentOccupancy() - 1);
            room.setAvailable(true);
            roomService.save(room);
        }

        Hostel hostel = room.getHostel();
        hostel.setAvailableRooms(
                (int) hostel.getRooms().stream().filter(Room::isAvailable).count()
        );

        user.setRoom(null);
        booking.setStatus(Status.CANCELLED);
        bookingRepository.save(booking);
    }
}
