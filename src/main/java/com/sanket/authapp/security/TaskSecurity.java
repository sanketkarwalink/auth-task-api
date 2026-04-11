package com.sanket.authapp.security;

import com.sanket.authapp.repository.TaskRepository;
import org.springframework.stereotype.Component;

@Component
public class TaskSecurity {
    private final TaskRepository taskRepository;

    public TaskSecurity(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    public boolean isOwner(Long taskId, String email){
        return taskRepository.findById(taskId)
                .map(task -> task.getUser() != null && task.getUser().getEmail().equals(email))
                .orElse(false);
    }
}
