package com.example.campus_nest_backend.utils;

public enum Capacity {
    SINGLE(1),
    DOUBLE(2),
    TRIPLE(3),
    QUAD(4);

    private final int value;

    Capacity(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public static Capacity fromValue(int value) {
        for (Capacity capacity : values()) {
            if (capacity.value == value) {
                return capacity;
            }
        }
        throw new IllegalArgumentException("Invalid capacity value: " + value);
    }
}
