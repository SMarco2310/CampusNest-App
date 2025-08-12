package com.example.campus_nest_backend.exception;

public class UserHasActiveBookingException extends RuntimeException {
    public UserHasActiveBookingException(String message) {
        super(message);
    }
}
