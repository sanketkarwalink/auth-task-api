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
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TaskService {
    private final UserRepository userRepository;
    private final TaskRepository taskRepository;
    private static final Logger log = LoggerFactory.getLogger(TaskService.class);


    public TaskService(UserRepository userRepository, TaskRepository taskRepository) {
        this.userRepository = userRepository;
        this.taskRepository = taskRepository;
    }


    //Create Task
    public ApiResponse createTask(TaskRequest request, String currentUserEmail){

        User user = userRepository.findByEmail(currentUserEmail);

        if(user == null){
            log.warn("User not found with email: {}", currentUserEmail);
            throw new ResourceNotFoundException("User not found");
        }
        Task task = new Task();
        task.setTitle(request.getTitle());
        task.setDescription(request.getDescription());
        task.setStatus(TaskStatus.PENDING);

        task.setUser(user);

        taskRepository.save(task);
        log.info("Task created successfully for user: {}", currentUserEmail);
        return new ApiResponse("success","Task created successfully");
    }

    //GetMyTask
    public List<TaskResponse> getMyTasks(String currentUserEmail){

        return taskRepository.findByUser_Email(currentUserEmail)
                .stream()
                .map(tasks -> new TaskResponse(
                        tasks.getId(),
                        tasks.getTitle(),
                        tasks.getDescription(),
                        tasks.getStatus()
                )).toList();
    }

    //Delete task
    @PreAuthorize("@taskSecurity.isOwner(#id, authentication.name)")
    @Transactional
    public ApiResponse deleteTask(Long id){

        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Task not found"));


        taskRepository.delete(task);
        log.info("Task deleted successfully with id: {}", id);
        return new ApiResponse("success", "Task deleted successfully");
    }

    //Simple update task
    @PreAuthorize("@taskSecurity.isOwner(#taskId, authentication.name)")
    @Transactional
    public ApiResponse updateTask(Long taskId){
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new ResourceNotFoundException("Task not found"));

        task.toggleStatus();

        return new ApiResponse("success", "Task updated successfully");
    }
}
