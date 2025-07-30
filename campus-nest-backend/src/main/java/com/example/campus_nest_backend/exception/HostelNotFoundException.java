package com.example.campus_nest_backend.exception;

public class HostelNotFoundException extends RuntimeException{
    public HostelNotFoundException(String message) {
        super(message);
    }

}
