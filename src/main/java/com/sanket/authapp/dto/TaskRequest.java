package com.sanket.authapp.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class TaskRequest {
    @NotBlank(message = "title cannot be empty")
    @Size(min = 3, max = 50, message = "Size of title must be between 3 and 50 characters")
    private String title;

    @NotBlank(message = "Description cannot be empty")
    private String description;

    public String getTitle(){
        return title;
    }
    public String getDescription(){
        return description;
    }
}
