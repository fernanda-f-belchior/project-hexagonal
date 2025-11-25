package com.example.userapi.adapter.out.repository;

import com.example.userapi.application.mapper.UserMapper;
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


    private final UserMapper userMapper = new UserMapper();

    public UserRepositoryAdapter(UserJpaRepository jpa) {
        this.jpa = jpa;
    }

    public User save(User user) {
        log.debug("Persistindo usuário: {}", user);
        UserEntity entity = userMapper.convertToUserEntity(user);
        return userMapper.convertToUser(jpa.save(entity));
    }

    public Optional<User> findById(Long id) {
        log.debug("Buscando usuário com ID: {}", id);
        Optional<UserEntity> userEntity = jpa.findById(id);
        UserEntity user = userEntity.orElse(new UserEntity());

        return Optional.of(userMapper.convertToUser(user));
    }

    public List<User> findAll() {
        log.debug("Consultando todos os usuários no banco de dados...");
        return jpa.findAll().stream()
                .map(userMapper::convertToUser)
                .toList();

    }

    public void deleteById(Long id) {
        log.debug("Excluindo usuário com ID: {}", id);
        jpa.deleteById(id);
    }



}
