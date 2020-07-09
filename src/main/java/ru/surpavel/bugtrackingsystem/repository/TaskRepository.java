package ru.surpavel.bugtrackingsystem.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import ru.surpavel.bugtrackingsystem.entity.Task;

public interface TaskRepository extends JpaRepository<Task, Long> {

    Page<Task> findByUserId(Long userId, Pageable pageable);
    
    Page<Task> findByProjectId(Long projectId, Pageable pageable);
}
