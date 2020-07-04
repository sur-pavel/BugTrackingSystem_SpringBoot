package ru.surpavel.bugtrackingsystem.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import ru.surpavel.bugtrackingsystem.domain.*;

public interface UserDao extends JpaRepository<User, Long> {

    List<Project> findByProjectId(Long projectid);
}
