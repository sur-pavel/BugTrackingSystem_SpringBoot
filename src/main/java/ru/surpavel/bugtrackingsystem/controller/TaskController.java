package ru.surpavel.bugtrackingsystem.controller;

import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import ru.surpavel.bugtrackingsystem.entity.Task;
import ru.surpavel.bugtrackingsystem.exception.ResourceNotFoundException;
import ru.surpavel.bugtrackingsystem.repository.TaskRepository;
import ru.surpavel.bugtrackingsystem.repository.UserRepository;
import ru.surpavel.bugtrackingsystem.service.TaskService;

@Controller
public class TaskController {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private final int ROW_PER_PAGE = 5;
    
    @Autowired
    private TaskService taskService;
    
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TaskRepository taskRepository;

    @GetMapping("/tasks/add")
    public String showAddTask(Model model) {
        Task task = new Task();
        model.addAttribute("add", true);
        model.addAttribute("task", task);
        return "tasks/task-edit";
    }

    @PostMapping("/tasks/add")
    public String addTask(Model model, @ModelAttribute("task") Task task) {        
        try {
            Task newTask = taskService.save(task);
            return "redirect:/tasks/" + String.valueOf(newTask.getId());
        } catch (Exception ex) {
            String errorMessage = ex.getMessage();
            logger.error(errorMessage);
            model.addAttribute("errorMessage", errorMessage);
            model.addAttribute("add", true);
            return "tasks/task-edit";
        }        
    }

    @GetMapping("/tasks/{taskId}/edit")
    public String showEditTask(Model model, @PathVariable long taskId) {
        Task task = null;
        try {
            task = taskService.findById(taskId);
        } catch (ResourceNotFoundException ex) {
            model.addAttribute("errorMessage", "Task not found");
        }
        model.addAttribute("add", false);
        model.addAttribute("task", task);
        return "tasks/task-edit";
    }

    @PostMapping("/tasks/{taskId}/edit")
    public String updateTask(Model model, @PathVariable long taskId, @ModelAttribute("task") Task task) {        
        try {
            task.setId(taskId);
            taskService.update(task);
            return "redirect:/tasks/" + String.valueOf(task.getId());
        } catch (Exception ex) {
            String errorMessage = ex.getMessage();
            logger.error(errorMessage);
            model.addAttribute("errorMessage", errorMessage);
            model.addAttribute("add", false);
            return "tasks/task-edit";
        }
    }
    
    @GetMapping("/tasks")
    public String findAllTasks(Model model,
            @RequestParam(value = "page", defaultValue = "1") int pageNumber) {
        List<Task> tasks = taskService.findAll(pageNumber, ROW_PER_PAGE);
        long count = taskService.count();
        boolean hasPrev = pageNumber > 1;
        boolean hasNext = (pageNumber * ROW_PER_PAGE) < count;
        model.addAttribute("tasks", tasks);
        model.addAttribute("hasPrev", hasPrev);
        model.addAttribute("prev", pageNumber - 1);
        model.addAttribute("hasNext", hasNext);
        model.addAttribute("next", pageNumber + 1);
        return "tasks/task-list";
    }

    
    @GetMapping("/tasks/{taskId}")

    public String findTaskById(Model model, @PathVariable long taskId) {
        Task task = null;
        try {
            task = taskService.findById(taskId);
        } catch (ResourceNotFoundException ex) {
            model.addAttribute("errorMessage", "Task not found");
        }
        model.addAttribute("task", task);
        return "tasks/task";
    }    
    
    @GetMapping("/tasks/{taskId}/delete")
    public String showDeleteTaskById(Model model, @PathVariable long taskId) {
        Task task = null;
        try {
            task = taskService.findById(taskId);
        } catch (ResourceNotFoundException ex) {
            model.addAttribute("errorMessage", "Task not found");
        }
        model.addAttribute("allowDelete", true);
        model.addAttribute("task", task);
        return "tasks/task";
    }

    @PostMapping("/tasks/{taskId}/delete")
    public String deleteTaskById(Model model, @PathVariable long taskId) {
        try {
            taskService.deleteById(taskId);
            return "redirect:/tasks";
        } catch (ResourceNotFoundException ex) {
            String errorMessage = ex.getMessage();
            logger.error(errorMessage);
            model.addAttribute("errorMessage", errorMessage);
            return "tasks/task";
        }
    }
}