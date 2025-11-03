package com.exemple.projectapi.port;

import com.exemple.projectapi.domain.model.User;

import java.util.List;
import java.util.Optional;

public interface UserRepositoryPort {
    User save(User user);
    Optional<User> findById(Long id);
    List<User> findAll();
    void deleteById(Long id);
    User update(Long id, User user);
}
