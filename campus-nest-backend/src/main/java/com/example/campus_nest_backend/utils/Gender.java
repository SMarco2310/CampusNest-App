package com.example.campus_nest_backend.utils;

public enum Gender {

    FEMALE("Female"),
    MALE("Male");

    private final String genderName;

    Gender(String genderName) {
        this.genderName = genderName;
    }

    public String getGenderName() {
        return genderName;
    }


}
