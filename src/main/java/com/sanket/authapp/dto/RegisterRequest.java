package com.sanket.authapp.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class RegisterRequest {

    private String name;
    @Email(message = "Invalid email format")
    @NotBlank(message = "Email is required")
    private String email;

    @NotBlank(message = "Password is required")
    @Size(min = 6, message = "Password must be of minimum 6 characters")
    @Pattern(
            regexp = "^(?=.*[0-9]).*$",
            message = "Password must contain at least one number"
    )
    private String password;


    // getters only (no setter needed for now)
    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getName(){
        return name;
    }
}