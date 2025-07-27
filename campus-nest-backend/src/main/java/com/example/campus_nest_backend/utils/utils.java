package com.example.campus_nest_backend.utils;

import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

public  class utils {
    public static PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    // add asysnc method to update the database like the number of users and other stuffs

    @Async
    public void updateDatabase() {
        // Logic to update the database asynchronously
        // This could involve updating user counts, statistics, etc.
        // For example, you might fetch some data and save it back to the database.
    }
}
