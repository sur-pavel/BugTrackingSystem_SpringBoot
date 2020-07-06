package ru.surpavel.bugtrackingsystem.repository;



import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import ru.surpavel.bugtrackingsystem.entity.Project;

public interface ProjectRepository extends JpaRepository<Project, Long> {

    Project findByTitle(String title);

    Optional<Project> findByTitlePageable(String projectTitle);
    
    

}
