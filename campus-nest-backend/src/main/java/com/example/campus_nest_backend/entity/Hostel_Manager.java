package com.example.campus_nest_backend.entity;

import com.example.campus_nest_backend.utils.Role;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Entity
@DiscriminatorValue("HOSTEL_MANAGER")
public class Hostel_Manager extends User{

    private List<Hostel> OwnedHostel;

    public Hostel_Manager() {
        setRole(Role.HOSTEL_MANAGER);
    }
}
