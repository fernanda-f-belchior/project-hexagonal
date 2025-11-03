package com.exemple.projectapi.application.service;

import com.exemple.projectapi.domain.model.User;
import com.exemple.projectapi.port.UserRepositoryPort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@Service
public class UserService {
    private final UserRepositoryPort repository;

    public UserService(UserRepositoryPort repository) {
        this.repository = repository;
    }

    public User save(User user) {
        return repository.save(user);
    }

    public Optional<User> findById(Long id) {
        return repository.findById(id);
    }

    public List<User> findAll() {
        return repository.findAll();
    }

    public User update(Long id, User user){return repository.update(id, user);

    }
    public void deleteById(Long id) {
        repository.deleteById(id);
    }


}
