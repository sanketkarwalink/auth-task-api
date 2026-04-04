package com.sanket.authapp.controller;

import com.sanket.authapp.dto.*;
import com.sanket.authapp.entity.Task;
import com.sanket.authapp.entity.User;
import com.sanket.authapp.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/auth")
public class AuthController {
    @Autowired
    private final AuthService authService;
    public AuthController(AuthService authService) {this.authService = authService;}

    @PostMapping("/register")
    public ResponseEntity<ApiResponse> register(@RequestBody @Valid RegisterRequest request) {
        ApiResponse response = authService.register(request);
        if(response.getStatus().equals("success")){
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } else{
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }
    @PostMapping("/login")
    public ResponseEntity<ApiResponse> login(@RequestBody @Valid LoginRequest request ) {
        ApiResponse response = authService.login(request);
        if(response.getStatus().equals("success")){
            return ResponseEntity.ok(response);
        }
        else{
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }
    }
    @GetMapping("/hello")
    public String hello(){
        return "Hello secured user";
    }

    @GetMapping("/users")
    public List<UserResponse> getUsers() {return authService.getAllUsers();}

    @GetMapping("/users/{id}")
    public User getUserById(@PathVariable Long id){
        return authService.getUserById(id);
    }

    @PutMapping("/users/{id}")
    public User updateUser(@PathVariable Long id, @RequestBody User user) {
        return authService.updateUser(id, user);
    }

    @DeleteMapping("/users/{id}")
    public String deleteUser(@PathVariable Long id){
        return authService.deleteUser(id);
    }

    @PostMapping("/api/tasks/{id}")
    public ApiResponse createTask(@PathVariable Long id, @RequestBody TaskRequest task){
        return authService.createTask(id,task);
    }

    @GetMapping("/api/tasks/{id}")
    public List<TaskResponse> getTasks(@PathVariable Long id){
        return authService.getTasksByUser(id);
    }
}

