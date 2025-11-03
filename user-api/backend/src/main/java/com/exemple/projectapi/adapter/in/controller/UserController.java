package com.exemple.projectapi.adapter.in.controller;

import com.exemple.projectapi.application.service.UserService;
import com.exemple.projectapi.application.service.ValidatorService;
import com.exemple.projectapi.domain.model.User;
import com.exemple.projectapi.domain.exception.BusinessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/users")
public class UserController {
    private final UserService service;
    private final ValidatorService validatorService;


    public UserController(UserService service, ValidatorService validatorService) {
        this.service = service;
        this.validatorService = validatorService;
    }

    @PostMapping
    public ResponseEntity<User> create(@RequestBody User user) {

        if (!validatorService.isRegistrationAllowed()) {
            throw new BusinessException("Cliente não disponivel no rollout");

        }

        return ResponseEntity.ok(service.save(user));

    }

    @GetMapping
    public List<User> list() {
        return service.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> get(@PathVariable Long id) {
        return service.findById(id).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Long id, @RequestBody User user) {
        Optional<User> userFound = service.findById(id);
        if(userFound.isEmpty()){
            String message = "User not found";
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(message);}

            return ResponseEntity.ok(service.update(id, user));
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        service.deleteById(id);
    }

}