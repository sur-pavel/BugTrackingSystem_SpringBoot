package ru.surpavel.bugtrackingsystem.controller;

import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import ru.surpavel.bugtrackingsystem.entity.Project;
import ru.surpavel.bugtrackingsystem.entity.Task;
import ru.surpavel.bugtrackingsystem.entity.User;
import ru.surpavel.bugtrackingsystem.repository.ProjectRepository;
import ru.surpavel.bugtrackingsystem.repository.ResourceNotFoundException;
import ru.surpavel.bugtrackingsystem.repository.TaskRepository;
import ru.surpavel.bugtrackingsystem.repository.UserRepository;

@RestController
public class ProjectController {

    @Autowired
    private ProjectRepository projectRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private TaskRepository taskRepository;

    @GetMapping("/projects")
    public Page<Project> getAllProjects(Pageable pageable) {
        return projectRepository.findAll(pageable);
    }

    @PostMapping("/projects")
    public Project createProject(@Valid @RequestBody Project project) {
        return projectRepository.save(project);
    }

    @GetMapping("/projects/{projectId}")
    public Optional<Project> getProject(@PathVariable Long projectId) {
        return projectRepository.findById(projectId);
    }

    @GetMapping("/projects/{projectId}/users")
    public Page<User> getAllProjectUsers(@PathVariable (value = "projectId") Long projectId,
                                                Pageable pageable) {
        return userRepository.findByProjectId(projectId, pageable);
    }
    
    @GetMapping("/projects/{projectId}/tasks")
    public Page<Task> getAllProjectTasks(@PathVariable (value = "projectId") Long projectId,
                                                Pageable pageable) {
        return taskRepository.findByProjectId(projectId, pageable);
    }
    
    @PutMapping("/projects/{projectId}")
    public Project updateProject(@PathVariable Long projectId, @Valid @RequestBody Project projectRequest) {
        return projectRepository.findById(projectId).map(project -> {
            project.setTitle(projectRequest.getTitle());
            return projectRepository.save(project);
        }).orElseThrow(() -> new ResourceNotFoundException("ProjectId " + projectId + " not found"));
    }

    @DeleteMapping("/projects/{projectId}")
    public ResponseEntity<?> deleteProject(@PathVariable Long projectId) {
        return projectRepository.findById(projectId).map(project -> {
            projectRepository.delete(project);
            return ResponseEntity.ok().build();
        }).orElseThrow(() -> new ResourceNotFoundException("ProjectId " + projectId + " not found"));
    }

   
}