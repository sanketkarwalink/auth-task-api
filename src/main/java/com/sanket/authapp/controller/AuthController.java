package com.sanket.authapp.controller;

import com.sanket.authapp.dto.ApiResponse;
import com.sanket.authapp.dto.LoginRequest;
import com.sanket.authapp.dto.RegisterRequest;
import com.sanket.authapp.dto.UserResponse;
import com.sanket.authapp.entity.User;
import com.sanket.authapp.repository.UserRepository;
import com.sanket.authapp.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/auth")
public class AuthController {
    @Autowired
    private final AuthService authService;
    public AuthController(AuthService authService) {this.authService = authService;}

    @PostMapping("/register")
    public ApiResponse register(@RequestBody @Valid RegisterRequest request) {
        return authService.register(request);
    }
    @PostMapping("/login")
    public ApiResponse login(@RequestBody @Valid LoginRequest request ) {
        return authService.login(request);
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
}
