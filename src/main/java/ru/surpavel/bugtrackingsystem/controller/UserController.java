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
import ru.surpavel.bugtrackingsystem.entity.User;
import ru.surpavel.bugtrackingsystem.repository.ProjectRepository;
import ru.surpavel.bugtrackingsystem.repository.ResourceNotFoundException;
import ru.surpavel.bugtrackingsystem.repository.TaskRepository;
import ru.surpavel.bugtrackingsystem.repository.UserRepository;

@RestController
public class UserController {

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TaskRepository taskRepository;

    @PostMapping("/projects/{projectId}/users")
    public User createUser(@PathVariable(value = "projectId") Long projectId, @Valid User user) {
        if (!projectRepository.existsById(projectId)) {
            throw new ResourceNotFoundException("ProjectId " + projectId + " not found");
        }
        user.setProject(projectRepository.findById(projectId).get());
        return userRepository.save(user);
    }

    @GetMapping("/users")
    public Page<User> findAllUsers(Pageable pageable) {
        return userRepository.findAll(pageable);
    }

    @GetMapping("/users/{userId}")
    public Optional<User> findUserById(@PathVariable Long userId) {
        return userRepository.findById(userId);
    }

    @GetMapping("/users/{userId}/tasks")
    public Page<Task> findUserTasks(@PathVariable(value = "userId") Long userId, Pageable pageable) {
        return taskRepository.findByUserId(userId, pageable);
    }

    @PutMapping("/projects/{projectId}/users/{userId}")
    public User updateUser(@PathVariable(value = "projectId") Long projectId,
            @PathVariable(value = "userId") Long userId, @Valid User userRequest) {
        if (!userRepository.existsById(projectId)) {
            throw new ResourceNotFoundException("ProjectId " + projectId + " not found");
        }
        userRequest.setProject(projectRepository.findById(projectId).get());
        return userRepository.findById(userId).map(user -> {
            user.setFirstName(userRequest.getFirstName());
            user.setLastName(userRequest.getLastName());
            return userRepository.save(user);
        }).orElseThrow(() -> new ResourceNotFoundException("UserId " + userId + "not found"));
    }

    @DeleteMapping("/users/{userId}")
    public ResponseEntity<?> deleteUser(@PathVariable Long userId) {
        return userRepository.findById(userId).map(user -> {
            userRepository.delete(user);
            return ResponseEntity.ok().build();
        }).orElseThrow(() -> new ResourceNotFoundException("UserId " + userId + " not found"));
    }
}