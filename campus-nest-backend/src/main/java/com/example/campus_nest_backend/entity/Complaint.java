//package com.example.campus_nest_backend.entity;
//
//
//import jakarta.persistence.*;
//
//@Entity
//@Table(name = "complaints")
//public class Complaint {
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long id;
//
//    private String content;
//    private String status; // e.g., "PENDING", "RESOLVED", "REJECTED"
//    private String response; // Admin's response to the complaint
//
//    @ManyToOne
//    @JoinColumn(name = "student_id", nullable = false)
//    private Student student; // The user who made the complaint
//
//    @ManyToOne
//    @JoinColumn(name = "admin_id", nullable = true)
//    private Admin admin; // The admin who handled the complaint, if applicable
//}
