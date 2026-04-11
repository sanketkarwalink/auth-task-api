package com.sanket.authapp.service;

import com.sanket.authapp.dto.*;
import com.sanket.authapp.entity.User;
import com.sanket.authapp.exception.BadRequestException;
import com.sanket.authapp.exception.ResourceNotFoundException;
import com.sanket.authapp.repository.UserRepository;
import com.sanket.authapp.security.JwtService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


@Service
public class AuthService {
    private static final Logger log = LoggerFactory.getLogger(AuthService.class);
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final JwtService jwtService;

    //constructor
    public AuthService(UserRepository userRepository, BCryptPasswordEncoder bCryptPasswordEncoder, JwtService jwtService) {this.userRepository = userRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.jwtService = jwtService;
    }


    //register
    public ApiResponse register(RegisterRequest request) {
        User existingUser = userRepository.findByEmail(request.getEmail());
        if (existingUser != null){
            throw new BadRequestException("Email already exists");
        }
        User user = new User();
        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setPassword(bCryptPasswordEncoder.encode(request.getPassword()));
        log.info("Register request for email: {}", request.getEmail());
        userRepository.save(user);

        return new ApiResponse("success","User registered successfully");
    }


    //login
    public ApiResponse login(LoginRequest request) {
        User user = userRepository.findByEmail(request.getEmail());
        if(user == null){
            log.warn("User not found with email: {}", request.getEmail());
            throw new ResourceNotFoundException("User not found");
        } else{
            if(bCryptPasswordEncoder.matches(request.getPassword(), user.getPassword())){
                String token = jwtService.generateToken(user.getEmail());
                log.info("User logged in successfully: {}", user.getEmail());
                return new ApiResponse("success",token);
            } else{
                log.warn("Login failed for email: {}", request.getEmail());
                throw new BadRequestException("Incorrect password");
            }
        }
    }
}
