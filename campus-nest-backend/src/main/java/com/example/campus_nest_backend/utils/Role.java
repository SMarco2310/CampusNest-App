package com.example.campus_nest_backend.utils;

public enum Role {
    STUDENT("Student"),
    ADMIN("Admin"),
    HOSTEL_MANAGER("Hostel Manager");

    private final String roleName;


    Role(String roleName) {
        this.roleName = roleName;
    }

    public static Role fromValue(String value) {
        for (Role capacity : values()) {
            if (capacity.roleName.equals(value)) {
                return capacity;
            }
        }
        throw new IllegalArgumentException("Invalid role value: " + value);
    }
}
