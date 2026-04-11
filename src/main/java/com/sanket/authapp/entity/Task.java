package com.sanket.authapp.entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
public class Task {
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    private String title;
    private String description;

    // we will use simple string for now
    @Enumerated(EnumType.STRING)
    private TaskStatus status;


}