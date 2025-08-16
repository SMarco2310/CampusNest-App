package com.example.campus_nest_backend.entity;

import com.example.campus_nest_backend.utils.Role;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@DiscriminatorValue("HOSTEL_MANAGER")
public class Hostel_Manager extends User{
    @OneToMany(mappedBy = "manager", cascade = CascadeType.ALL, orphanRemoval = true)
    List<BankAccountDetails> bankAccountDetails = new ArrayList<>();

    @OneToMany(mappedBy = "manager", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Hostel> ownedHostels = new ArrayList<>();


    public Hostel_Manager() {
        setRole(Role.HOSTEL_MANAGER);
    }
}
