package com.example.userapi.adapter.out.repository;

import com.example.userapi.domain.model.User;
import com.example.userapi.port.UserRepositoryPort;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import java.util.List;
import java.util.Optional;

@Slf4j
@Component
public class UserRepositoryAdapter implements UserRepositoryPort {
    private final UserJpaRepository jpa;

    public UserRepositoryAdapter(UserJpaRepository jpa) {
        this.jpa = jpa;
    }

    public User save(User user) {
        log.debug("Persistindo usuário: {}", user);
        UserEntity entity = new UserEntity();
        entity.setId(user.getId());
        entity.setEmail(user.getEmail());
        entity.setName(user.getName());

        return toDomain(jpa.save(entity));
    }

    public Optional<User> findById(Long id) {
        log.debug("Buscando usuário com ID: {}", id);
        return jpa.findById(id).map(this::toDomain);
    }

    public List<User> findAll() {

        log.debug("Consultando todos os usuários no banco de dados...");
        List<User> users = jpa.findAll().stream().map(this::toDomain).toList();
        return users;
    }

    public void deleteById(Long id) {
        log.debug("Excluindo usuário com ID: {}", id);
        jpa.deleteById(id);
    }

    public User update(Long id, User user){
        log.debug("Atualizando usuário com ID: {} no banco de dados.", id);
        user.setId(id);
        return this.save(user);
    }

    private User toDomain(UserEntity entity) {
        return new User(entity.getId(), entity.getName(), entity.getEmail());
    }

}
