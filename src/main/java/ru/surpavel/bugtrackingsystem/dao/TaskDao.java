package ru.surpavel.bugtrackingsystem.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import ru.surpavel.bugtrackingsystem.domain.*;

public interface TaskDao extends JpaRepository<Task, Long> {

    List<User> findByUserId(Long userId);
}
