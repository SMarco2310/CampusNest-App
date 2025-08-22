package com.example.campus_nest_backend.repository;

import com.example.campus_nest_backend.entity.Hostel_Manager;
import com.example.campus_nest_backend.entity.User;
import com.example.campus_nest_backend.utils.Role;
import jakarta.validation.constraints.Email;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Arrays;
import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    // Method to find a user by email
    User findByEmail(String email);

    User findUserById(Long id);
    // Method to check if a user exists by email
    boolean existsByEmail(String email);

    boolean existsByEmailAndIdNot(@Email String email, Long id);

    List<User> findAllByRole(Role role);

    Hostel_Manager findUserByIdAndRole(Long hostelManagerId,Role role);
}
