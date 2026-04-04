package com.sanket.authapp.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public class LoginRequest {

    @Email(message = "Invalid email format")
    @NotBlank(message = "Email is required")
    private String email;

    @NotBlank(message = "Password is required")
    private String password;

    public void  setEmail(String email) {
        this.email = email;
    }
    public String getEmail() {return email;}
    public void setPassword(String password) {this.password = password;}
    public String getPassword() {return password;}
}
