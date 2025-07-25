package com.example.campus_nest_backend.utils;

public enum Role {
    STUDENT("Student"),
    ADMIN("Admin"),
    HOSTEL_MANAGER("Hostel Manager");

    private final String roleName;


    Role(String roleName) {
        this.roleName = roleName;
    }
}
