package com.example.campus_nest_backend.entity;


import com.example.campus_nest_backend.utils.Role;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@DiscriminatorValue("STUDENT")
public class Student extends User{
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "room_id")
    private Room currentRoom;

    @NotBlank
    @Column(nullable = false , unique = true)
    private String studentId;

    @NotBlank
    @Column(nullable = false)
    private String course;

    @NotBlank
    @Column(name = "class_year", nullable = false)

    private String classYear;


    @OneToMany(mappedBy = "student", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Booking> bookings = new ArrayList<>();
    @OneToMany(mappedBy = "student", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Review> reviews = new ArrayList<>();



    public Student() {
        setRole(Role.STUDENT);
    }

}
