package com.example.campus_nest_backend.exception;

public class UserHasARoomAlreadyException extends RuntimeException {
    public UserHasARoomAlreadyException(String message) {
        super(message);
    }
}
