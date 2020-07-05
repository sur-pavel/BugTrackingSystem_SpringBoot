package ru.surpavel.bugtrackingsystem.repository;



import org.springframework.data.jpa.repository.JpaRepository;

import ru.surpavel.bugtrackingsystem.entity.Project;

public interface ProjectRepository extends JpaRepository<Project, Long> {
    
    

}
