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
import org.springframework.web.bind.annotation.RestController;

import ru.surpavel.bugtrackingsystem.entity.Task;
import ru.surpavel.bugtrackingsystem.repository.ProjectRepository;
import ru.surpavel.bugtrackingsystem.repository.ResourceNotFoundException;
import ru.surpavel.bugtrackingsystem.repository.TaskRepository;
import ru.surpavel.bugtrackingsystem.repository.UserRepository;

@RestController
public class TaskController {

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TaskRepository taskRepository;

    @PostMapping("/projects/{projectId}/users/{userId}/tasks")
    public Task createTask(@PathVariable(value = "projectId") Long projectId,
            @PathVariable(value = "userId") Long userId, @Valid Task task) {
        if (!projectRepository.existsById(projectId)) {
            throw new ResourceNotFoundException("ProjectId " + projectId + " not found");
        }
        if (!userRepository.existsById(userId)) {
            throw new ResourceNotFoundException("UserId " + userId + " not found");
        }
        task.setProject(projectRepository.findById(projectId).get());
        task.setUser(userRepository.findById(userId).get());
        return taskRepository.save(task);
    }

    @GetMapping("/tasks")
    public Page<Task> findAllTasks(Pageable pageable) {
        return taskRepository.findAll(pageable);
    }

    @GetMapping("/tasks/{taskId}")
    public Optional<Task> findTaskById(@PathVariable Long taskId) {
        return taskRepository.findById(taskId);
    }

    @PutMapping("/projects/{projectId}/users/{userId}/tasks/{taskId}")
    public Task updateTask(@PathVariable(value = "projectId") Long projectId,
            @PathVariable(value = "userId") Long userId, @PathVariable(value = "taskId") Long taskId,
            @Valid Task taskRequest) {
        if (!projectRepository.existsById(projectId)) {
            throw new ResourceNotFoundException("ProjectId " + projectId + " not found");
        }
        if (!userRepository.existsById(userId)) {
            throw new ResourceNotFoundException("UserId " + userId + " not found");
        }
        return taskRepository.findById(taskId).map(task -> {
            task.setTheme(taskRequest.getTheme());
            task.setTaskType(taskRequest.getTaskType());
            task.setPriority(taskRequest.getPriority());
            task.setDescription(taskRequest.getDescription());
            task.setProject(projectRepository.findById(projectId).get());
            task.setUser(userRepository.findById(userId).get());
            return taskRepository.save(task);
        }).orElseThrow(() -> new ResourceNotFoundException("TaskId " + taskId + "not found"));
    }

    @DeleteMapping("/tasks/{taskId}")
    public ResponseEntity<?> deleteTask(@PathVariable Long taskId) {
        return taskRepository.findById(taskId).map(task -> {
            taskRepository.delete(task);
            return ResponseEntity.ok().build();
        }).orElseThrow(() -> new ResourceNotFoundException("TaskId " + taskId + " not found"));
    }
}