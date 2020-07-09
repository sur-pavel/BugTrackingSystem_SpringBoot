package ru.surpavel.bugtrackingsystem.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import ru.surpavel.bugtrackingsystem.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {

    Page<User> findByProjectId(Long userId, Pageable pageable);
}
