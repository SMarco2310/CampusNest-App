package com.example.campus_nest_backend.exception;

public class UserNotFoundException extends RuntimeException{
    public UserNotFoundException(String message) {super(message);}
}
