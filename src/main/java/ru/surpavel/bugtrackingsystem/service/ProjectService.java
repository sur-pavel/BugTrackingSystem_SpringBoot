package ru.surpavel.bugtrackingsystem.service;

import java.util.ArrayList;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.data.domain.PageRequest;

import org.springframework.data.domain.Pageable;

import org.springframework.data.domain.Sort;

import org.springframework.stereotype.Service;

import org.springframework.util.StringUtils;

import ru.surpavel.bugtrackingsystem.entity.Project;
import ru.surpavel.bugtrackingsystem.exception.BadResourceException;
import ru.surpavel.bugtrackingsystem.exception.ResourceAlreadyExistsException;
import ru.surpavel.bugtrackingsystem.exception.ResourceNotFoundException;
import ru.surpavel.bugtrackingsystem.repository.ProjectRepository;

@Service
public class ProjectService {

    @Autowired
    private ProjectRepository projectRepository;

    private boolean existsById(Long id) {
        return projectRepository.existsById(id);
    }

    public Project findById(Long id) throws ResourceNotFoundException {
        Project project = projectRepository.findById(id).orElse(null);
        if (project==null) {
            throw new ResourceNotFoundException("Cannot find Project with id: " + id);
        }
        else return project;
    }

    public List<Project> findAll(int pageNumber, int rowPerPage) {
        List<Project> projects = new ArrayList<>();
        Pageable sortedByIdAsc = PageRequest.of(pageNumber - 1, rowPerPage, Sort.by("id").ascending());
        projectRepository.findAll(sortedByIdAsc).forEach(projects::add);
        return projects;
    }

    public Project save(Project project) throws BadResourceException, ResourceAlreadyExistsException {
        if (!StringUtils.isEmpty(project.getTitle())) {
            if (project.getId() != null && existsById(project.getId())) { 
                throw new ResourceAlreadyExistsException("Project with id: " + project.getId() + " already exists");
            }
            return projectRepository.save(project);
        }
        else {
            BadResourceException exc = new BadResourceException("Failed to save project. Project is null or empty");
            throw exc;
        }
    }

    public void update(Project project) 
            throws BadResourceException, ResourceNotFoundException {
        if (!StringUtils.isEmpty(project.getTitle())) {
            if (!existsById(project.getId())) {
                throw new ResourceNotFoundException("Cannot find Project with id: " + project.getId());
            }
            projectRepository.save(project);
        }
        else {
            BadResourceException exc = new BadResourceException("Failed to save project. Project is null or empty");
            throw exc;
        }
    }

    public void deleteById(Long id) throws ResourceNotFoundException {
        if (!existsById(id)) { 
            throw new ResourceNotFoundException("Cannot find project with id: " + id);
        }
        else {
            projectRepository.deleteById(id);
        }
    }

    public Long count() {
        return projectRepository.count();
    }
}