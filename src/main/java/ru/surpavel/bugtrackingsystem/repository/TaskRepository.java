package ru.surpavel.bugtrackingsystem.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import ru.surpavel.bugtrackingsystem.entity.*;

public interface TaskRepository extends JpaRepository<Task, Long> {

    List<Task> findByUserId(Long userId);
    List<Task> findByProjectId(Long projectid);
}
