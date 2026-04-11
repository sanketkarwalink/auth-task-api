package com.sanket.authapp.controller;

import com.sanket.authapp.dto.UserResponse;
import com.sanket.authapp.entity.User;
import com.sanket.authapp.service.UserService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    //get all users
    @GetMapping("/")
    public List<UserResponse> getUsers() {return userService.getAllUsers();}


    //get user by id
    @GetMapping("/{id}")
    public User getUserById(@PathVariable Long id){
        return userService.getUserById(id);
    }

    //update user
    @PutMapping("/{id}")
    public User updateUser(@PathVariable Long id, @RequestBody User user) {
        return userService.updateUser(id, user);
    }

    //Delete user
    @DeleteMapping("/{id}")
    public String deleteUser(@PathVariable Long id){
        return userService.deleteUser(id);
    }
}
