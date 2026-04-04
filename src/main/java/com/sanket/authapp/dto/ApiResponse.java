package com.sanket.authapp.dto;

public class ApiResponse {

    public String status;
    public String message;

    public ApiResponse(String status, String message) {
        this.status = status;
        this.message = message;
    }

    public String getStatus() {return status;}
    public String getMessage() {return message;}
}
