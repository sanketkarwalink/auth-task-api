package com.sanket.authapp.service;

import com.sanket.authapp.dto.*;
import com.sanket.authapp.entity.Task;
import com.sanket.authapp.entity.TaskStatus;
import com.sanket.authapp.entity.User;
import com.sanket.authapp.exception.BadRequestException;
import com.sanket.authapp.exception.ResourceNotFoundException;
import com.sanket.authapp.repository.TaskRepository;
import com.sanket.authapp.repository.UserRepository;
import com.sanket.authapp.security.JwtService;
import jakarta.validation.Valid;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AuthService {
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final TaskRepository taskRepository;
    private final JwtService jwtService;
    public AuthService(UserRepository userRepository, BCryptPasswordEncoder bCryptPasswordEncoder, TaskRepository taskRepository, JwtService jwtService) {this.userRepository = userRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.taskRepository = taskRepository;
        this.jwtService = jwtService;
    }

    public ApiResponse register(RegisterRequest request) {
        User existingUser = userRepository.findByEmail(request.getEmail());
        if (existingUser != null){
            throw new BadRequestException("Email already exists");
        }
        User user = new User();
        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setPassword(bCryptPasswordEncoder.encode(request.getPassword()));
        userRepository.save(user);
        return new ApiResponse("success","User registered successfully");
    }

    public ApiResponse login(LoginRequest request) {
        User user = userRepository.findByEmail(request.getEmail());
        if(user == null){
            throw new ResourceNotFoundException("User not found");
        } else{
            if(bCryptPasswordEncoder.matches(request.getPassword(), user.getPassword())){
                String token = jwtService.generateToken(user.getEmail());
                return new ApiResponse("success",token);
            } else{
                return new  ApiResponse("Error","Incorrect password");
            }
        }
    }

    public List<UserResponse> getAllUsers() {
        return userRepository.findAll()
                .stream()
                .map(user -> new UserResponse(user.getId(), user.getName(), user.getEmail()))
                .toList();
    }

    public User getUserById(Long id){
        return userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
    }

    public User updateUser(Long id, User updatedUser){
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        user.setEmail(updatedUser.getEmail());
        user.setPassword(bCryptPasswordEncoder.encode(updatedUser.getPassword()));

        return userRepository.save(user);
    }

    public String deleteUser(Long id){
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        userRepository.delete(user);

        return "User deleted successfully";
    }

    public ApiResponse createTask(TaskRequest request){
        String currentEmail = SecurityContextHolder.getContext().getAuthentication().getName();

        User user = userRepository.findByEmail(currentEmail);

        if(user == null){
            throw new ResourceNotFoundException("User not found");
        }
        Task task = new Task();
        task.setTitle(request.getTitle());
        task.setDescription(request.getDescription());
        task.setStatus(TaskStatus.PENDING);

        task.setUser(user);

        taskRepository.save(task);

        return new ApiResponse("success","Task created successfully");
    }

    public List<TaskResponse> getMyTasks(){
        String email = SecurityContextHolder.getContext().getAuthentication().getName();

        return taskRepository.findByUser_Email(email)
                .stream()
                .map(tasks -> new TaskResponse(
                        tasks.getId(),
                        tasks.getTitle(),
                        tasks.getDescription(),
                        tasks.getStatus()
                )).toList();
    }

    public ApiResponse deleteTask(Long id){
        String currentUserEmail = SecurityContextHolder.getContext().getAuthentication().getName();

        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Task not found"));

        if (!task.getUser().getEmail().equals(currentUserEmail)) {
            throw new BadRequestException("You are not authorized to delete this task!");
        }
        taskRepository.delete(task);
        return new ApiResponse("success", "Task deleted successfully");
    }

    public ApiResponse updateTask(Long taskId){
        String currentUserEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new ResourceNotFoundException("Task not found"));

        if(!task.getUser().getEmail().equals(currentUserEmail)){
            return new ApiResponse("error", "You are not authorized to update this task!");
        }

        if(task.getStatus()==TaskStatus.PENDING){
            task.setStatus(TaskStatus.COMPLETED);
        }else{
            task.setStatus(TaskStatus.PENDING);
        }

        taskRepository.save(task);
        return new ApiResponse("success", "Task updated successfully" + task.getStatus());
    }
}
