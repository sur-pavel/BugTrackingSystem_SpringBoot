package ru.surpavel.bugtrackingsystem.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import ru.surpavel.bugtrackingsystem.domain.Project;

public interface ProjectDao extends JpaRepository<Project, Long> {

}
