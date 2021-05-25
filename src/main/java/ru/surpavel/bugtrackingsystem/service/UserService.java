package ru.surpavel.bugtrackingsystem.service;

import java.util.ArrayList;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.data.domain.PageRequest;

import org.springframework.data.domain.Pageable;

import org.springframework.data.domain.Sort;

import org.springframework.stereotype.Service;

import org.springframework.util.StringUtils;

import ru.surpavel.bugtrackingsystem.entity.User;
import ru.surpavel.bugtrackingsystem.exception.BadResourceException;
import ru.surpavel.bugtrackingsystem.exception.ResourceAlreadyExistsException;
import ru.surpavel.bugtrackingsystem.exception.ResourceNotFoundException;
import ru.surpavel.bugtrackingsystem.repository.UserRepository;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    private boolean existsById(Long id) {
        return userRepository.existsById(id);
    }

    public User findById(Long id) throws ResourceNotFoundException {
        User user = userRepository.findById(id).orElse(null);
        if (user==null) {
            throw new ResourceNotFoundException("Cannot find User with id: " + id);
        }
        else return user;
    }

    public List<User> findAll(int pageNumber, int rowPerPage) {
        List<User> users = new ArrayList<>();
        Pageable sortedByIdAsc = PageRequest.of(pageNumber - 1, rowPerPage, Sort.by("id").ascending());
        userRepository.findAll(sortedByIdAsc).forEach(users::add);
        return users;
    }

    public User save(User user) throws BadResourceException, ResourceAlreadyExistsException {
        if (!StringUtils.isEmpty(user.getFirstName())&&!StringUtils.isEmpty(user.getLastName())) {
            if (user.getId() != null && existsById(user.getId())) { 
                throw new ResourceAlreadyExistsException("User with id: " + user.getId() + " already exists");
            }
            return userRepository.save(user);
        }
        else {
            BadResourceException exc = new BadResourceException("Failed to save user. User is null or empty");
            throw exc;
        }
    }

    public void update(User user) 
            throws BadResourceException, ResourceNotFoundException {
        if (!StringUtils.isEmpty(user.getFirstName())&&!StringUtils.isEmpty(user.getLastName())) {
            if (!existsById(user.getId())) {
                throw new ResourceNotFoundException("Cannot find User with id: " + user.getId());
            }
            userRepository.save(user);
        }
        else {
            BadResourceException exc = new BadResourceException("Failed to save user. User is null or empty");
            throw exc;
        }
    }

    public void deleteById(Long id) throws ResourceNotFoundException {
        if (!existsById(id)) { 
            throw new ResourceNotFoundException("Cannot find user with id: " + id);
        }
        else {
            userRepository.deleteById(id);
        }
    }

    public Long count() {
        return userRepository.count();
    }
}