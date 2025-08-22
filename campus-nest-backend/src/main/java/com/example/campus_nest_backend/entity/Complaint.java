package com.example.campus_nest_backend.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "complaints")
public class Complaint {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true,name = "content", length = 2000)
    private String content;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Status status = Status.PENDING; // "PENDING", "RESOLVED", "REJECTED"

    private String response; // Admin's response to the complaint

    @ManyToOne
    @JoinColumn(name = "student_id", nullable = false)
    private Student student; // The student who made the complaint

    @ManyToOne
    @JoinColumn(name = "hostel_Manager_id", nullable = true)
    private Hostel_Manager hostel; // The hostel related to the complaint

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        if (this.status == null) {
            this.status = Status.PENDING;
        }
    }
}

enum Status {
    PENDING,
    RESOLVED,
    RECEIVED,
    REJECTED;

}
