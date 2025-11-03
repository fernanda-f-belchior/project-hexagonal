package com.exemple.projectapi.adapter.out.repository;

import com.exemple.projectapi.domain.model.User;
import com.exemple.projectapi.port.UserRepositoryPort;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class UserRepositoryAdapter implements UserRepositoryPort {
    private final UserJpaRepository jpa;

    public UserRepositoryAdapter(UserJpaRepository jpa) {
        this.jpa = jpa;
    }

    public User save(User user) {
        UserEntity entity = new UserEntity();
        entity.setId(user.getId());
        entity.setEmail(user.getEmail());
        entity.setName(user.getName());

        return toDomain(jpa.save(entity));
    }

    public Optional<User> findById(Long id) {
        return jpa.findById(id).map(this::toDomain);
    }

    public List<User> findAll() {
        return jpa.findAll().stream().map(this::toDomain).toList();
    }

    public void deleteById(Long id) {
        jpa.deleteById(id);
    }

    public User update(Long id, User user){


        user.setId(id);
        return this.save(user);
    }

    private User toDomain(UserEntity entity) {
        return new User(entity.getId(), entity.getName(), entity.getEmail());
    }

}
