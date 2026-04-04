package com.sanket.authapp.dto;

public class UserResponse {
    private Long id;
    private String email;
    private String name;

    public UserResponse(Long id, String name, String email) {this.id = id; this.email = email; this.name = name;}
    public String getName(){
        return name;
    }
    public Long getId() {return id;}
    public String getEmail(){return email;}

}
