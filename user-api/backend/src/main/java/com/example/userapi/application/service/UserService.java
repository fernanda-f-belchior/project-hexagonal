package com.example.userapi.application.service;

import com.example.userapi.application.usecases.UserUseCase;
import com.example.userapi.domain.exception.BusinessException;
import com.example.userapi.domain.model.User;
import com.example.userapi.port.UserRepositoryPort;
import com.example.userapi.port.ValidatorPort;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class UserService implements UserUseCase {
    private final UserRepositoryPort repository;
    private final ValidatorPort validatorPort;

    public UserService(UserRepositoryPort repository, ValidatorPort validatorPort) {
        this.repository = repository;
        this.validatorPort = validatorPort;
    }

    @Override
    public User save(User user) {
        log.debug("Validando dados para criação");
        if (user.getName() == null || user.getEmail() == null) {
            throw new BusinessException("Nome e email são obrigatórios", HttpStatus.UNPROCESSABLE_ENTITY);
        }
        if (!validatorPort.isRegistrationAllowed()) {
            log.warn("Usuário não cadastrado pois não foi validado no rollout.");
            throw new BusinessException( "Usuário não disponivel no rollout", HttpStatus.UNPROCESSABLE_ENTITY);
        }

        User savedUser = repository.save(user);
        log.info("Usuário cadastrado com sucesso! ID: {}", savedUser.getId());
        return savedUser;

    }

    public User findById(Long id) {
        log.debug("Buscando usuário com ID: {}", id);
        User user = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Usuário não encontrado"));
        log.debug("Usuário com ID: {} encontrado", id);
        return user;
    }

    public List<User> findAll() {
        log.debug("Iniciando consulta de usuários...");
        List<User> users = repository.findAll();
        log.info("Consulta finalizada. Total de usuários encontrados: {}", users.size());
        return users;
    }

    public User update(Long id, User user){
        log.debug("Atualizando usuário com ID: {}", id);
        this.findById(id);
        User updated = repository.update(id, user);
        log.info("Usuário com ID: {} atualizado com sucesso", id);
        return updated;

    }

    public void deleteById(Long id) {
        log.debug("Excluindo usuário com ID: {}", id);
        this.findById(id);
        repository.deleteById(id);
        log.debug("Usuário com ID: {} removido com sucesso.", id);
    }


}
