package com.sanket.authapp.service;

import com.sanket.authapp.dto.ApiResponse;
import com.sanket.authapp.dto.TaskRequest;
import com.sanket.authapp.dto.TaskResponse;
import com.sanket.authapp.entity.Task;
import com.sanket.authapp.entity.TaskStatus;
import com.sanket.authapp.entity.User;
import com.sanket.authapp.exception.BadRequestException;
import com.sanket.authapp.exception.ResourceNotFoundException;
import com.sanket.authapp.repository.TaskRepository;
import com.sanket.authapp.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TaskService {
    private final UserRepository userRepository;
    private final TaskRepository taskRepository;
    private static final Logger log = LoggerFactory.getLogger(AuthService.class);


    public TaskService(UserRepository userRepository, TaskRepository taskRepository) {
        this.userRepository = userRepository;
        this.taskRepository = taskRepository;
    }


    //Create Task
    public ApiResponse createTask(TaskRequest request){
        String currentEmail = SecurityContextHolder.getContext().getAuthentication().getName();

        User user = userRepository.findByEmail(currentEmail);

        if(user == null){
            log.warn("User not found with email: {}", currentEmail);
            throw new ResourceNotFoundException("User not found");
        }
        Task task = new Task();
        task.setTitle(request.getTitle());
        task.setDescription(request.getDescription());
        task.setStatus(TaskStatus.PENDING);

        task.setUser(user);

        taskRepository.save(task);
        log.info("Task created successfully for user: {}", currentEmail);
        return new ApiResponse("success","Task created successfully");
    }

    //GetMyTask
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

    //Delete task
    public ApiResponse deleteTask(Long id){
        String currentUserEmail = SecurityContextHolder.getContext().getAuthentication().getName();

        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Task not found"));

        if (!task.getUser().getEmail().equals(currentUserEmail)) {
            throw new BadRequestException("You are not authorized to delete this task!");
        }
        taskRepository.delete(task);
        log.info("Task deleted successfully with id: {}", id);
        return new ApiResponse("success", "Task deleted successfully");
    }

    //Simple update task
    @Transactional
    public ApiResponse updateTask(Long taskId){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if(auth == null || !auth.isAuthenticated()){
            throw new BadRequestException("User not authenticated");
        }
        String currentUserEmail = auth.getName();
        log.info("Current user: {}", currentUserEmail);
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new ResourceNotFoundException("Task not found"));

        if(task.getUser() == null || task.getUser().getEmail() == null){
            throw new IllegalStateException("Task user not set properly");
        }

        if(!task.getUser().getEmail().equals(currentUserEmail)){
            throw new BadRequestException("You are not authorized to update this task");
        }

        task.toggleStatus();

        return new ApiResponse("success", "Task updated successfully");
    }
}
