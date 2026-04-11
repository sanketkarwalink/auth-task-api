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
    @Column(nullable = false)
    private TaskStatus status;

    public void toggleStatus(){
        this.status = (this.status == TaskStatus.PENDING)
                ? TaskStatus.COMPLETED
                : TaskStatus.PENDING;
    }


}