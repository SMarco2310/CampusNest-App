package com.example.campus_nest_backend.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class SecurityConfig {

    private final JwtFilter jwtFilter;

    public SecurityConfig(JwtFilter jwtFilter) {
        this.jwtFilter = jwtFilter;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                                "/api/auth/login",
                                "/api/auth/register",
                                "/api/hostels/hostels",          // GET all hostels
                                "/api/hostels/details/{hostelId}", // GET hostel details
                                "/api/rooms/{hostelId}",           // GET rooms by hostel
                                "/api/rooms/details/{roomId}"      // GET room details
                        ).permitAll()

                        // Admin-only endpoints
                        .requestMatchers(
                                "/api/users/users",                // GET all users
                                "/api/users/delete/{userId}",      // DELETE user
                                "/api/users/update/{userId}"       // PUT update user
                        ).hasRole("ADMIN")

                        // Hostel manager endpoints
                        .requestMatchers(
                                "/api/hostels/create",             // POST create hostel
                                "/api/hostels/hostel/{hostelId}",  // PUT update hostel
                                "/api/hostels/delete/{hostelId}",  // DELETE hostel
                                "/api/rooms/add",                  // POST add room
                                "/api/rooms/update/room/{roomId}", // PUT update room
                                "/api/rooms/delete/room/{roomId}"  // DELETE room
                        ).hasRole("HOSTEL_MANAGER")

                        // User-specific endpoints
                        .requestMatchers(
                                "/api/users/{userId}",             // GET user by ID
                                "/api/bookings/bookings/{userId}", // GET user bookings
                                "/api/bookings/booking",           // POST create booking
                                "/api/bookings/update/booking/{id}", // PUT update booking
                                "/api/bookings/delete/booking/{id}", // DELETE booking
                                "/api/review/{hostelId}/reviews",  // GET reviews by hostel
                                "/api/review/review/{id}",         // GET review by ID
                                "/api/review/review",              // POST add review
                                "/api/review/review/{id}",        // PUT update review
                                "/api/review/review/{id}"         // DELETE review
                        ).hasAnyRole("STUDENT", "HOSTEL_MANAGER", "ADMIN")
                        .anyRequest().authenticated()
                )
                .sessionManagement(sess -> sess
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                );

        http.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    // Needed to authenticate credentials manually
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }


}
