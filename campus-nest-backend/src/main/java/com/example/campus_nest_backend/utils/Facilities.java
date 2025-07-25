package com.example.campus_nest_backend.utils;

public enum Facilities {
    WIFI("Wi-Fi"),
    PARKING("Parking"),
    GYM("Gym"),
    LAUNDRY("Laundry"),
    CAFETERIA("Cafeteria"),
    SECURITY("Security"),
    STUDY_ROOM("Study Room"),
    COMMON_ROOM("Common Room");

    private final String facilityName;

    Facilities(String facilityName) {
        this.facilityName = facilityName;
    }

    public String getFacilityName() {
        return facilityName;
    }
}
