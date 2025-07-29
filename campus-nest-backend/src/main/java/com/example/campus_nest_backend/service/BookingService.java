package com.example.campus_nest_backend.service;

import com.example.campus_nest_backend.dto.BookingRequest;
import com.example.campus_nest_backend.entity.Booking;
import com.example.campus_nest_backend.repository.BookingRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookingService {
    private final BookingRepository bookingRepository;
    private final UserService userService;
    private final RoomService roomService;
    public BookingService(BookingRepository bookingRepository, UserService userService, RoomService roomService) {
        this.bookingRepository = bookingRepository;
        this.userService = userService;
        this.roomService = roomService;
    }

    public Booking createBooking(BookingRequest bookingRequest){
        Booking booking = new Booking();
        booking.setUser(userService.getUserById(bookingRequest.getUserId()));
        booking.setRoom(roomService.getRoomById(bookingRequest.getRoomId()));
        booking.setBookingDate(bookingRequest.getBookingDate());
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

    public Booking updateBooking(Long id, BookingRequest bookingRequest){
        Booking booking = bookingRepository.findById(id).orElse(null);
        if(booking == null){
            throw new RuntimeException("this booking does not exist");
        }
        booking.setBookingDate(bookingRequest.getBookingDate());
        booking.setRoom(roomService.getRoomById(bookingRequest.getRoomId()));
        booking.setUser(userService.getUserById(bookingRequest.getUserId()));
        return bookingRepository.save(booking);
    }


    public void deleteBooking(Long id){
        bookingRepository.deleteById(id);
    }




}
