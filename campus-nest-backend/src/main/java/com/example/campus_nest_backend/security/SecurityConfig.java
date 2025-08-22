package com.example.campus_nest_backend.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
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
                        // Public endpoints
                        .requestMatchers(
                                "/api/auth/**",
                                "/api/hostels/summary",
                                "/api/hostels/{id}",
                                "/api/hostels/{hostelId}/rooms",
                                "/api/rooms/{roomId}",
                                "/api/reviews/{id}",
                                "/api/reviews",
                                "/api/admin/login",
                                "/api/admin/register",
                                "/api/payments/webhook"
                        ).permitAll()

                        // Admin-only endpoints
                        .requestMatchers(
                                "/api/admin/**",        // all admin actions except login/register
                                "/api/users",
                                "/api/users/{id}",
                                "/api/bookings"
                        ).hasRole("ADMIN")

                        // Hostel Manager-only endpoints
                        .requestMatchers(
                                "/api/hostels",
                                "/api/hostels/{id}",
                                "/api/rooms",
                                "/api/rooms/{roomId}",
                                "/api/bookings/{bookingId}",
                                "/api/rooms/occupant/room/{roomId}"
                        ).hasRole("HOSTEL_MANAGER")

                        // Complaints – Admin + Hostel Manager
                        .requestMatchers(
                                "/api/complaints",
                                "/api/complaints/{id}",
                                "/api/bookings/room/{roomId}",

                                "/api/complaints/student/{studentId}",
                                "/api/bookings/hostel/{hostelId}",

                                "/api/complaints/hostelManager/{hostelManagerId}"
                        ).hasAnyRole("ADMIN", "HOSTEL_MANAGER")

                        // Authenticated users (student, manager, admin)
                        .requestMatchers(
                                "/api/users/{id}",
                                "/api/users/{id}/password",
                                "/api/bookings/user/{userId}",
                                "/api/bookings",
                                "/api/reviews",
                                "/api/reviews/{id}",
                                "/api/payments/initialize",
                                "/api/payments/verify/**",
                                "/api/complaints" // create complaint
                        ).hasAnyRole("STUDENT", "HOSTEL_MANAGER", "ADMIN")

                        // All other requests
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
