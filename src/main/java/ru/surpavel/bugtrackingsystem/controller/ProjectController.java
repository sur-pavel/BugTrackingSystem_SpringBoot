package ru.surpavel.bugtrackingsystem.controller;

import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.surpavel.bugtrackingsystem.entity.Project;
import ru.surpavel.bugtrackingsystem.entity.Task;
import ru.surpavel.bugtrackingsystem.entity.User;
import ru.surpavel.bugtrackingsystem.exception.ResourceNotFoundException;
import ru.surpavel.bugtrackingsystem.repository.TaskRepository;
import ru.surpavel.bugtrackingsystem.repository.UserRepository;
import ru.surpavel.bugtrackingsystem.service.ProjectService;


@Controller
public class ProjectController {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private final int ROW_PER_PAGE = 5;
    
    @Autowired
    private ProjectService projectService;
    
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TaskRepository taskRepository;

    @GetMapping("/projects/add")
    public String showAddProject(Model model) {
        Project project = new Project();
        model.addAttribute("add", true);
        model.addAttribute("project", project);
        return "projects/project-edit";
    }

    @PostMapping("/projects/add")
    public String addProject(Model model, @ModelAttribute("project") Project project) {        
        try {
            Project newProject = projectService.save(project);
            return "redirect:/projects/" + String.valueOf(newProject.getId());
        } catch (Exception ex) {
            String errorMessage = ex.getMessage();
            logger.error(errorMessage);
            model.addAttribute("errorMessage", errorMessage);
            model.addAttribute("add", true);
            return "projects/project-edit";
        }        
    }

    @GetMapping("/projects/{projectId}/edit")
    public String showEditProject(Model model, @PathVariable long projectId) {
        Project project = null;
        try {
            project = projectService.findById(projectId);
        } catch (ResourceNotFoundException ex) {
            model.addAttribute("errorMessage", "Project not found");
        }
        model.addAttribute("add", false);
        model.addAttribute("project", project);
        return "projects/project-edit";
    }

    @PostMapping("/projects/{projectId}/edit")
    public String updateProject(Model model, @PathVariable long projectId, @ModelAttribute("project") Project project) {        
        try {
            project.setId(projectId);
            projectService.update(project);
            return "redirect:/projects/" + String.valueOf(project.getId());
        } catch (Exception ex) {
            String errorMessage = ex.getMessage();
            logger.error(errorMessage);
            model.addAttribute("errorMessage", errorMessage);
            model.addAttribute("add", false);
            return "projects/project-edit";
        }
    }
    
    @GetMapping("/projects")
    public String findAllProjects(Model model,
            @RequestParam(value = "page", defaultValue = "1") int pageNumber) {
        List<Project> projects = projectService.findAll(pageNumber, ROW_PER_PAGE);
        long count = projectService.count();
        boolean hasPrev = pageNumber > 1;
        boolean hasNext = (pageNumber * ROW_PER_PAGE) < count;
        model.addAttribute("projects", projects);
        model.addAttribute("hasPrev", hasPrev);
        model.addAttribute("prev", pageNumber - 1);
        model.addAttribute("hasNext", hasNext);
        model.addAttribute("next", pageNumber + 1);
        return "projects/project-list";
    }

    
    @GetMapping("/projects/{projectId}")

    public String findProjectById(Model model, @PathVariable long projectId) {
        Project project = null;
        try {
            project = projectService.findById(projectId);
        } catch (ResourceNotFoundException ex) {
            model.addAttribute("errorMessage", "Project not found");
        }
        model.addAttribute("project", project);
        return "projects/project";
    }    
    
    @GetMapping("/projects/{projectId}/users")
    public Page<User> findAllProjectUsers(@PathVariable(value = "projectId") Long projectId, Pageable pageable) {
        return userRepository.findByProjectId(projectId, pageable);
    }

    @GetMapping("/projects/{projectId}/tasks")
    public Page<Task> findAllProjectTasks(@PathVariable(value = "projectId") Long projectId, Pageable pageable) {
        return taskRepository.findByProjectId(projectId, pageable);
    }

    @GetMapping("/projects/{projectId}/delete")
    public String showDeleteProjectById(Model model, @PathVariable long projectId) {
        Project project = null;
        try {
            project = projectService.findById(projectId);
        } catch (ResourceNotFoundException ex) {
            model.addAttribute("errorMessage", "Project not found");
        }
        model.addAttribute("allowDelete", true);
        model.addAttribute("project", project);
        return "projects/project";
    }

    @PostMapping("/projects/{projectId}/delete")
    public String deleteProjectById(Model model, @PathVariable long projectId) {
        try {
            projectService.deleteById(projectId);
            return "redirect:/projects";
        } catch (ResourceNotFoundException ex) {
            String errorMessage = ex.getMessage();
            logger.error(errorMessage);
            model.addAttribute("errorMessage", errorMessage);
            return "projects/project";
        }
    }
}