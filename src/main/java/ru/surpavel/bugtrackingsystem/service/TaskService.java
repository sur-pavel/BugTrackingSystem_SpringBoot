package ru.surpavel.bugtrackingsystem.service;

import java.util.ArrayList;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.data.domain.PageRequest;

import org.springframework.data.domain.Pageable;

import org.springframework.data.domain.Sort;

import org.springframework.stereotype.Service;

import org.springframework.util.StringUtils;

import ru.surpavel.bugtrackingsystem.entity.Task;
import ru.surpavel.bugtrackingsystem.exception.BadResourceException;
import ru.surpavel.bugtrackingsystem.exception.ResourceAlreadyExistsException;
import ru.surpavel.bugtrackingsystem.exception.ResourceNotFoundException;
import ru.surpavel.bugtrackingsystem.repository.TaskRepository;

@Service

public class TaskService {

    @Autowired
    private TaskRepository taskRepository;
    
    private boolean existsById(Long id) {
        return taskRepository.existsById(id);
    }

    public Task findById(Long id) throws ResourceNotFoundException {
        Task task = taskRepository.findById(id).orElse(null);
        if (task==null) {
            throw new ResourceNotFoundException("Cannot find Task with id: " + id);
        }
        else return task;
    }

    public List<Task> findAll(int pageNumber, int rowPerPage) {
        List<Task> tasks = new ArrayList<>();
        Pageable sortedByIdAsc = PageRequest.of(pageNumber - 1, rowPerPage, Sort.by("id").ascending());
        taskRepository.findAll(sortedByIdAsc).forEach(tasks::add);
        return tasks;
    }

    public Task save(Task task) throws BadResourceException, ResourceAlreadyExistsException {
        if (!StringUtils.isEmpty(task.getTheme())) {
            if (task.getId() != null && existsById(task.getId())) { 
                throw new ResourceAlreadyExistsException("Task with id: " + task.getId() + " already exists");
            }
            return taskRepository.save(task);
        }
        else {
            BadResourceException exc = new BadResourceException("Failed to save task. Task is null or empty");
            throw exc;
        }
    }

    public void update(Task task) 
            throws BadResourceException, ResourceNotFoundException {
        if (!StringUtils.isEmpty(task.getTheme())) {
            if (!existsById(task.getId())) {
                throw new ResourceNotFoundException("Cannot find Task with id: " + task.getId());
            }
            taskRepository.save(task);
        }
        else {
            BadResourceException exc = new BadResourceException("Failed to save task. Task is null or empty");
            throw exc;
        }
    }

    public void deleteById(Long id) throws ResourceNotFoundException {
        if (!existsById(id)) { 
            throw new ResourceNotFoundException("Cannot find task with id: " + id);
        }
        else {
            taskRepository.deleteById(id);
        }
    }
    
    public Long count() {
        return taskRepository.count();
    }
}