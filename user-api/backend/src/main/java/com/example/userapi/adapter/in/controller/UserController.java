package com.example.userapi.adapter.in.controller;


import com.example.userapi.application.usecases.UserUseCase;
import com.example.userapi.domain.model.User;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/users")
@Tag(name = "usuários", description = "Operações relacionadas a usuários")
public class UserController {

    private final UserUseCase userUseCase;


    public UserController(UserUseCase userUseCase) {
        this.userUseCase = userUseCase;
    }

    @PostMapping
    @Operation(summary = "Endpoint responsável por adicionar um novo usuário.", description = "Retorna os dados do novo usuário adicionado.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Usuário encontrado"),
            @ApiResponse(responseCode = "422", description = "Usuário não disponível no rollout"),
            @ApiResponse(responseCode = "422", description = "Nome e email são obrigatórios")
    })
    public ResponseEntity<User> create(@RequestBody User user) {
        log.info("Recebida requisição POST /users");
        return ResponseEntity.ok(userUseCase.save(user));
    }

    @GetMapping
    @Operation(summary = "Endpoint responsável por retornar uma lista com todos os usuários cadastrados.", description = "Retorna lista de usuários.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista retornada"),
    })
    public List<User> list() {
        log.info("Recebida requisição GET /users");
        return userUseCase.findAll();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Endpoint responsável por buscar um usuário pelo id de cadastro.", description = "Retorna os dados do usuário pelo id de cadastro.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Usuário encontrado"),
            @ApiResponse(responseCode = "404", description = "Usuário não encontrado")
    })
    public ResponseEntity<User> findById(@PathVariable Long id) {
        log.info("Recebida requisição GET /user/{id}");
        return ResponseEntity.ok(userUseCase.findById(id));
    }

    @PutMapping("/{id}")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Usuário atualizado"),
            @ApiResponse(responseCode = "404", description = "Usuário não encontrado")
    })
    @Operation(summary = "Endpoint responsável por atualizar os dados de um usuário identificado pelo id.", description = "Retorna os dados atualizados do usuário.")
    public ResponseEntity<?> update(@PathVariable Long id, @RequestBody User user) {
        log.info("Recebida requisição PUT /users/{id}");
        return ResponseEntity.ok(userUseCase.update(id, user));
    }

    @DeleteMapping("/{id}")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Usuário excluído"),
            @ApiResponse(responseCode = "404", description = "Usuário não encontrado")
    })
    @Operation(summary = "Endpoint responsável por excluir os dados de um usuário identificado pelo id.", description = "Exclui dados do usuário.")
    public void delete(@PathVariable Long id) {
        log.info("Recebida requisição DELETE /users/{id}");
        userUseCase.deleteById(id);
    }

}
