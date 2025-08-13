package com.example.campus_nest_backend.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final JwtFilter jwtFilter;

    public SecurityConfig(JwtFilter jwtFilter) {
        this.jwtFilter = jwtFilter;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .cors()
                .and()
                .csrf().disable()
                .authorizeHttpRequests(auth -> auth
                        // Public endpoints (no auth needed)
                        .requestMatchers(
                                "/api/auth/**",
                                "/api/hostels/summary",
                                "/api/hostels/{id}",
                                "/api/hostels/{hostelId}/rooms",
                                "/api/rooms/{roomId}",
                                "/api/reviews/{id}",
                                "/api/reviews" // for POST review (maybe should be protected, but you decide)
                        ).permitAll()

                        // Admin-only endpoints
                        .requestMatchers(
                                "/api/users",              // GET all users
                                "/api/users/{id}",         // DELETE user
                                "/api/bookings",           // GET all bookings (admin overview)
                                "/api/bookings/room/{roomId}",  // GET bookings by room
                                "/api/bookings/hostel/{hostelId}" // GET bookings by hostel
                        ).hasRole("ADMIN")

                        // Hostel Manager-only endpoints
                        .requestMatchers(
                                "/api/hostels",            // POST create hostel
                                "/api/hostels/{id}",       // PUT update hostel
                                "/api/hostels/{id}",       // DELETE hostel
                                "/api/rooms",              // POST create room
                                "/api/rooms/{roomId}",     // PUT update room
                                "/api/rooms/{roomId}",     // DELETE room
                                "/api/bookings/{bookingId}", // PUT update booking (manager can update)
                                "/api/bookings/{bookingId}", // DELETE booking (cancel)
                                "/api/rooms/occupant/room/{roomId}" // if exists for manager
                        ).hasRole("HOSTEL_MANAGER")

                        // Endpoints accessible to any authenticated user (students, managers, admin)
                        .requestMatchers(
                                "/api/users/{id}",           // user details (allow self or roles)
                                "/api/users/{id}",         // GET user by id (could be user or admin)
                                "/api/users/{id}/password",// PUT update password
                                "/api/users/{id}",         // PUT update user
                                "/api/bookings/user/{userId}", // user bookings
                                "/api/bookings",             // POST create booking
                                "/api/reviews",              // POST review
                                "/api/reviews/{id}",         // PUT and DELETE review
                                "/api/reviews/{id}"          // GET review by id (public above, but repeated here for auth)
                        ).hasAnyRole("STUDENT", "HOSTEL_MANAGER", "ADMIN")

                        // All other requests must be authenticated
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
