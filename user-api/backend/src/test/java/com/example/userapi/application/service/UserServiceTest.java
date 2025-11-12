package com.example.userapi.application.service;

import com.example.userapi.domain.exception.BusinessException;
import com.example.userapi.domain.model.User;
import com.example.userapi.port.UserRepositoryPort;
import com.example.userapi.port.ValidatorPort;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserServiceTest {

    private UserRepositoryPort repository;
    private ValidatorPort validatorPort;
    private UserService service;

    @BeforeEach
    void setUp() {
        repository = mock(UserRepositoryPort.class);
        validatorPort = mock(ValidatorPort.class);
        service = new UserService(repository, validatorPort);
    }

    @Test
    void save_shouldThrowException_whenNameOrEmailIsNull() {
        User user = new User();
        user.setName(null);
        user.setEmail(null);

        BusinessException exception = assertThrows(BusinessException.class, () -> service.save(user));
        assertEquals("Nome e email são obrigatórios", exception.getMessage());
    }

    @Test
    void save_shouldThrowException_whenValidatorReturnsFalse() {
        User user = new User();
        user.setName("João");
        user.setEmail("joao@example.com");

        when(validatorPort.isRegistrationAllowed()).thenReturn(false);

        BusinessException exception = assertThrows(BusinessException.class, () -> service.save(user));
        assertEquals("Usuário não disponivel no rollout", exception.getMessage());
    }

    @Test
    void save_shouldPersistUser_whenValidatorReturnsTrue() {
        User user = new User();
        user.setName("Maria");
        user.setEmail("maria@example.com");

        User savedUser = new User();
        savedUser.setId(1L);
        savedUser.setName("Maria");
        savedUser.setEmail("maria@example.com");

        when(validatorPort.isRegistrationAllowed()).thenReturn(true);
        when(repository.save(user)).thenReturn(savedUser);

        User result = service.save(user);

        assertEquals(1L, result.getId());
        assertEquals("Maria", result.getName());
        verify(repository).save(user);
    }

    @Test
    void findById_shouldReturnUser_whenExists() {
        User user = new User();
        user.setId(1L);
        user.setName("Ana");

        when(repository.findById(1L)).thenReturn(Optional.of(user));
        User result = service.findById(1L);
        assertEquals("Ana", result.getName());
    }

    @Test
    void findById_shouldThrowNotFoundException_whenUserDoesNotExist() {
        when(repository.findById(99L)).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class, () -> service.findById(99L));
    }

    @Test
    void findAll_shouldReturnListOfUsers() {
        User firstUser = new User(); firstUser.setId(1L);
        User secondUser = new User(); secondUser.setId(2L);

        when(repository.findAll()).thenReturn(Arrays.asList(firstUser, secondUser));
        List<User> userList = service.findAll();
        assertEquals(2, userList.size());
    }

    @Test
    void update_shouldUpdateUser_whenExists() {
        User user01 = new User();
        user01.setId(1L);
        user01.setEmail("ana@email.com");
        user01.setName("Ana");

        User user02 = new User();
        user02.setId(1L);
        user02.setEmail("joana@email.com");
        user02.setName("Joana");

        when(repository.findById(1L)).thenReturn(Optional.of(user01));
        when(repository.update(1L, user02)).thenReturn(user02);

        User result = service.update(1L, user02);

        assertEquals("Joana", result.getName());
        assertEquals("joana@email.com", result.getEmail());
        verify(repository).update(1L, user02);

    }

    @Test
    void deleteById_shouldDeleteUser_whenExists() {
        when(repository.findById(1L)).thenReturn(Optional.of(new User()));
        service.deleteById(1L);
        verify(repository).deleteById(1L);
    }

    @Test
    void deleteById_shouldThrowNotFoundException_whenUserDoesNotExist() {
        when(repository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class, () -> service.deleteById(1L));
    }


}