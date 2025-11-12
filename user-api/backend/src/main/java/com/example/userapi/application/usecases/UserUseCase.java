package com.example.userapi.application.usecases;

import com.example.userapi.domain.model.User;

import java.util.List;
import java.util.Optional;

public interface UserUseCase {

    public User save(User user);
    public User update(Long id, User user);
    public void deleteById(Long id);
    public User findById(Long id);
    public List<User> findAll();

}
