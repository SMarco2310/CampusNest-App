package com.example.campus_nest_backend.utils;

public enum Status {
    PENDING("Pending"), CONFIRMED("Confirmed"), CANCELLED("Cancelled");

    private final String statusName;

    Status(String statusName) {
        this.statusName = statusName;
    }
}
