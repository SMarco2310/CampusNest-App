package com.example.campus_nest_backend.utils;

public enum Status {
    PENDING("Pending"), CONFIRMED("Confirmed"), CANCELLED("Cancelled");

    private final String statusName;

    Status(String statusName) {
        this.statusName = statusName;
    }

    public static Status fromValue(String value) {
        for (Status capacity : values()) {
            if (capacity.statusName.equals(value)) {
                return capacity;
            }
        }
        throw new IllegalArgumentException("Invalid status value: " + value);
    }
}
