package com.example.campus_nest_backend.entity;

import com.example.campus_nest_backend.utils.Role;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@DiscriminatorValue("ADMIN")
public class Admin extends User{
    // Admin-specific fields can be added here if needed
    // For now, Admin inherits all fields from User


    public Admin() {
        setRole(Role.ADMIN);
    }
}
