package com.sanket.authapp.dto;

import com.sanket.authapp.entity.TaskStatus;

public class TaskResponse {
    private Long id;
    private String title;
    private String description;
    private TaskStatus status;


    public TaskResponse(Long id, String title, String description, TaskStatus status) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.status = status;
    }

    public Long getId(){
        return id;
    }
    public String getTitle(){
        return title;
    }
    public String getDescription(){
        return description;
    }
    public TaskStatus getStatus(){
        return status;
    }
}
