package com.example.campus_nest_backend.entity;


import com.example.campus_nest_backend.utils.Role;
import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.NumberFormat;

import java.time.Year;
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

    @Column(unique = true)
    @NumberFormat(pattern = "########")
    private Long studentId;

    @Column(name = "course")
    private String course;

    @Column(name = "class_year")
    @NumberFormat(pattern = "####")
    private Integer classYear;


    @OneToMany(mappedBy = "student", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Booking> bookings = new ArrayList<>();
    @OneToMany(mappedBy = "student", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Review> reviews = new ArrayList<>();



    public Student() {
        setRole(Role.STUDENT);
    }

}
