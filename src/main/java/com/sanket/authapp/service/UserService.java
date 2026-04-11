package com.sanket.authapp.service;

import com.sanket.authapp.dto.UserResponse;
import com.sanket.authapp.entity.User;
import com.sanket.authapp.exception.ResourceNotFoundException;
import com.sanket.authapp.repository.UserRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public UserService(UserRepository userRepository, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.userRepository = userRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    //Get all users
    public List<UserResponse> getAllUsers() {
        return userRepository.findAll()
                .stream()
                .map(user -> new UserResponse(user.getId(), user.getName(), user.getEmail()))
                .toList();
    }

    //Get user by id
    public User getUserById(Long id){
        return userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
    }

    //Update user
    public User updateUser(Long id, User updatedUser){
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        user.setEmail(updatedUser.getEmail());
        user.setPassword(bCryptPasswordEncoder.encode(updatedUser.getPassword()));

        return userRepository.save(user);
    }

    //delete user
    public String deleteUser(Long id){
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        userRepository.delete(user);

        return "User deleted successfully";
    }


}
