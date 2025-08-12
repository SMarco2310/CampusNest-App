package com.example.campus_nest_backend.exception;

public class DuplicateHostelNameException extends RuntimeException {
    public DuplicateHostelNameException(String message) {
        super(message);
    }
}
