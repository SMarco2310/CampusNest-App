//package com.example.campus_nest_backend.entity;
//
//import jakarta.persistence.*;
//import lombok.Getter;
//import lombok.Setter;
//
//import java.util.List;
//
//@Entity
//@Setter
//@Getter
//public class Chat {
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long id;
//    @ManyToOne
//    @JoinColumn(name = "client_id")
//    private User client;
//    @ManyToOne
//    @JoinColumn(name="Manager_id")
//    private User Manager;
//
//    @OneToMany(mappedBy = "chat", cascade = CascadeType.ALL, orphanRemoval = true)
//    List<Message> messages;
//
//
//}
