package ru.surpavel.bugtrackingsystem.controller;

import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import ru.surpavel.bugtrackingsystem.entity.Task;
import ru.surpavel.bugtrackingsystem.repository.*;

@RestController
public class TaskController {

    @Autowired
    private ProjectRepository projectRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private TaskRepository taskRepository;

    @GetMapping("/tasks")
    public Page<Task> getAllTasks(Pageable pageable) {
        return taskRepository.findAll(pageable);
    }

    @PostMapping("/tasks")
    public Task createTask(@Valid @RequestBody Task task) {
        return taskRepository.save(task);
    }

    @GetMapping("/tasks/{taskId}")
    public Optional<Task> getTask(@PathVariable Long taskId) {
        return taskRepository.findById(taskId);
    }

    @PutMapping("/projects/{projectId}/user/{userId}/task/{taskId}")
    public Task updateUser(@PathVariable(value = "projectId") Long projectId,
            @PathVariable(value = "userId") Long userId, @PathVariable(value = "userId") Long taskId,
            @Valid @RequestBody Task taskRequest) {

        if (!projectRepository.existsById(projectId)) {
            throw new ResourceNotFoundException("ProjectId " + projectId + " not found");
        }

        if (!userRepository.existsById(userId)) {
            throw new ResourceNotFoundException("UserId " + userId + " not found");
        }

        return taskRepository.findById(taskId).map(task -> {
            task.setTheme(taskRequest.getTheme());
//            
//            
//            
            return taskRepository.save(task);
        }).orElseThrow(() -> new ResourceNotFoundException("UserId " + userId + "not found"));
    }

    @DeleteMapping("/tasks/{taskId}")
    public ResponseEntity<?> deleteTask(@PathVariable Long taskId) {
        return taskRepository.findById(taskId).map(task -> {
            taskRepository.delete(task);
            return ResponseEntity.ok().build();
        }).orElseThrow(() -> new ResourceNotFoundException("TaskId " + taskId + " not found"));
    }
}