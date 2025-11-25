package com.example.userapi.adapter.out.repository;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String email;

    @OneToMany(
            mappedBy = "userEntity",              // indica que o lado dono está em AddressEntity
            cascade = CascadeType.ALL,            // salva/deleta endereços junto com o usuário
            orphanRemoval = true                  // remove endereços órfãos automaticamente
    )
    private List<AddressEntity> addressList;

}