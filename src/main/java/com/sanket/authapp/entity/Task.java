package com.sanket.authapp.entity;

import jakarta.persistence.*;
import lombok.Setter;

@Entity
public class Task {
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // setters
    @Setter
    private String title;
    @Setter
    private String description;

    // we will use simple string for now
    @Setter
    private String status;

    // getters
    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getStatus() {
        return status;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

}