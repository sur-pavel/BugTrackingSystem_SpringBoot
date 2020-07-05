package ru.surpavel.bugtrackingsystem.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import ru.surpavel.bugtrackingsystem.entity.*;

public interface UserRepository extends JpaRepository<User, Long> {

    List<Project> findByProjectId(Long projectid);
}
