package com.example.campus_nest_backend.service;

import com.example.campus_nest_backend.dto.BookingRequest;
import com.example.campus_nest_backend.entity.Booking;
import com.example.campus_nest_backend.entity.Hostel;
import com.example.campus_nest_backend.entity.Room;
import com.example.campus_nest_backend.entity.User;
import com.example.campus_nest_backend.exception.BookingNotFoundException;
import com.example.campus_nest_backend.exception.RoomUnavailableException;
import com.example.campus_nest_backend.repository.BookingRepository;
import com.example.campus_nest_backend.utils.Status;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookingService {
    private final BookingRepository bookingRepository;
    private final UserService userService;
    private final RoomService roomService;
    private final EmailService emailService;
    public BookingService(BookingRepository bookingRepository, UserService userService, RoomService roomService, EmailService emailService) {
        this.bookingRepository = bookingRepository;
        this.userService = userService;
        this.roomService = roomService;
        this.emailService = emailService;
    }

    @Transactional
    public Booking bookRoom(BookingRequest bookingRequest) {
        Booking booking = new Booking();
        Room room = roomService.getRoomById(bookingRequest.getRoomId());
        User user = userService.getUserById(bookingRequest.getUserId());
        Hostel hostel = room.getHostel();
        if (!room.isAvailable() || room.getCurrentOccupancy() >= room.getCapacity()) {
            throw new RoomUnavailableException("Room is not available or is at full capacity");
        }

        //Update room state
        room.setCurrentOccupancy(room.getCurrentOccupancy() + 1);
        if (room.getCurrentOccupancy() >= room.getCapacity()) {
            room.setAvailable(false); // optional logic
        }
        booking.setUser(user);
        booking.setRoom(room);
        booking.setBookingDate(bookingRequest.getBookingDate());
        /*
        * I have to make this such that when the room isbooked the total number of run
        * stays the same but invrease when a new room is created in that hostel but the Available room is more about moving out or moving in
        *
        *
        * I also being able to handle the people leaving the rooms for good and yep the rest I have to think og
        * morrow
        *
        * */
        hostel.setAvailableRooms((int) hostel.getRooms().stream().filter(Room::isAvailable).count());
        emailService.sendSimpleEmail(user.getEmail(),"Thank You for booking with Us","Thank you for Booking with Us "+room.getHostel().getName());
        roomService.save(room); // make sure to persist room update
        return bookingRepository.save(booking);
    }


    public List<Booking> getAllBookings(){
        return bookingRepository.findAll();
    }

    public Booking findBooking(Long id){
         return bookingRepository.findById(id).orElse(null);
    }

    public List<Booking> findBookingsByUserId(Long userId){
        return bookingRepository.findBookingsByUserId(userId);
    }

    @Transactional
    public Booking updateBooking(Long id, BookingRequest bookingRequest) {
        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() -> new BookingNotFoundException("This booking does not exist"));

        Room oldRoom = booking.getRoom();
        Room newRoom = roomService.getRoomById(bookingRequest.getRoomId());
        User user = userService.getUserById(bookingRequest.getUserId());

        // If changing to a different room
        if (!oldRoom.getId().equals(newRoom.getId())) {
            // Decrease old room occupancy
            if (oldRoom.getCurrentOccupancy() > 0) {
                oldRoom.setCurrentOccupancy(oldRoom.getCurrentOccupancy() - 1);
                oldRoom.setAvailable(true); // Optional
                roomService.save(oldRoom);
            }

            // Check new room availability
            if (!newRoom.isAvailable() || newRoom.getCurrentOccupancy() >= newRoom.getCapacity()) {
                throw new RoomUnavailableException("New room is not available or full");
            }

            // Increase new room occupancy
            newRoom.setCurrentOccupancy(newRoom.getCurrentOccupancy() + 1);
            if (newRoom.getCurrentOccupancy() >= newRoom.getCapacity()) {
                newRoom.setAvailable(false);
            }
            roomService.save(newRoom);

            booking.setRoom(newRoom);
        }

        booking.setUser(user);
        emailService.sendSimpleEmail(user.getEmail(),"Booking Updated","Your Booking has been updated To "+ newRoom.getRoomNumber());

        booking.setBookingDate(bookingRequest.getBookingDate());

        return bookingRepository.save(booking);
    }

    @Transactional
    public void deleteBooking(Long id) {
        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() -> new BookingNotFoundException("This booking does not exist"));

        Room room = booking.getRoom();

        // Decrease occupancy if the booking is active
        if (room.getCurrentOccupancy() > 0) {
            room.setCurrentOccupancy(room.getCurrentOccupancy() - 1);
            room.setAvailable(true); // Optional: mark available again if space opens up
            roomService.save(room);
        }
        Hostel hostel = room.getHostel();
        hostel.setAvailableRooms(
                (int) hostel.getRooms().stream().filter(Room::isAvailable).count()
        );

        booking.setStatus(Status.CANCELLED);
        bookingRepository.save(booking);

    }





}
