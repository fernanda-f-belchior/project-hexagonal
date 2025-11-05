package com.example.userapi.application.service;

import com.example.userapi.domain.exception.BusinessException;
import com.example.userapi.domain.model.User;
import com.example.userapi.port.UserRepositoryPort;
import com.example.userapi.port.ValidatorPort;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class UserService {
    private final UserRepositoryPort repository;
    private final ValidatorPort validatorPort;

    public UserService(UserRepositoryPort repository, ValidatorPort validatorPort) {
        this.repository = repository;
        this.validatorPort = validatorPort;
    }

    public User save(User user) {
        if (!validatorPort.isRegistrationAllowed()) {
            throw new BusinessException( "Cliente não disponivel no rollout", HttpStatus.UNPROCESSABLE_ENTITY);
        }

        return repository.save(user);

    }

    public Optional<User> findById(Long id) {
        return repository.findById(id);
    }

    public List<User> findAll() {
        log.debug("Iniciando consulta de usuários...");
        List<User> users = repository.findAll();
        log.info("Consulta finalizada. Total de usuários encontrados: {}", users.size());
        return users;
    }

    public User update(Long id, User user){
        Optional<User> userFound = repository.findById(id);

        if (userFound.isEmpty()) {
            throw new BusinessException( "User not found", HttpStatus.NOT_FOUND);
        }

        return repository.update(id, user);


    }
    public void deleteById(Long id) {
        repository.deleteById(id);
    }


}
