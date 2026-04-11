package com.sanket.authapp.controller;

import com.sanket.authapp.dto.ApiResponse;
import com.sanket.authapp.dto.TaskRequest;
import com.sanket.authapp.dto.TaskResponse;
import com.sanket.authapp.service.TaskService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/tasks")
public class TaskController {
    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    //Create task
    @PostMapping("/create")
    public ResponseEntity<ApiResponse> createTask(@Valid @RequestBody TaskRequest task){
        ApiResponse response = taskService.createTask(task);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    //get my task
    @GetMapping("/tasks")
    public ResponseEntity<List<TaskResponse>> getTasks(){
        return ResponseEntity.ok(taskService.getMyTasks());
    }

    //delete task by id
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse> deleteTask(@PathVariable Long id){
        ApiResponse response = taskService.deleteTask(id);

        if(response.getStatus().equals("success")){
            return ResponseEntity.ok(response);
        }else{
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(response);
        }
    }


    //Simple update task
    @PatchMapping("/{id}/status")
    public ResponseEntity<ApiResponse> updateTaskStatus(@PathVariable Long id){
        return ResponseEntity.ok(taskService.updateTask(id));
    }
}
