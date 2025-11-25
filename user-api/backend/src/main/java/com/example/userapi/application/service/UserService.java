package com.example.userapi.application.service;

import com.example.userapi.application.dto.AddressDTO;
import com.example.userapi.application.dto.UserDTO;
import com.example.userapi.application.mapper.AddressMapper;
import com.example.userapi.application.mapper.UserMapper;
import com.example.userapi.application.response.PostalCodeAddressResponse;
import com.example.userapi.application.usecases.UserUseCase;
import com.example.userapi.domain.exception.BusinessException;

import com.example.userapi.port.PostalCodeAddressPort;
import com.example.userapi.port.UserRepositoryPort;
import com.example.userapi.port.RolloutValidatorPort;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;


import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class UserService implements UserUseCase {
    private final UserRepositoryPort repository;
    private final RolloutValidatorPort rolloutValidatorPort;
    private final PostalCodeAddressPort postalCodeAddressPort;
    private final UserMapper userMapper;
    private final AddressMapper addressMapper;



    public UserService(UserRepositoryPort repository, RolloutValidatorPort rolloutValidatorPort, PostalCodeAddressPort postalCodeAddressPort, UserMapper userMapper, AddressMapper addressMapper){

        this.repository = repository;
        this.rolloutValidatorPort = rolloutValidatorPort;
        this.postalCodeAddressPort = postalCodeAddressPort;
        this.userMapper = userMapper;
        this.addressMapper = addressMapper;
    }


    @Override
    public UserDTO save(UserDTO userDTO) {

        log.debug("Validando dados para criação");
        if ((userDTO.getName()==null || userDTO.getName().isBlank())
            ||( userDTO.getEmail() == null || userDTO.getEmail().isBlank())) {
            throw new BusinessException("Nome e email são obrigatórios", HttpStatus.UNPROCESSABLE_ENTITY);
        }

        /* Valida dados de endereço vindos da requisição e preenche demais atributos com
        a resposta da api OpenCep. */
        auxVerifyAndPopulateAddress(userDTO);

        //Verifica se o usuário está validado pelo rollout.
        if (!rolloutValidatorPort.isRegistrationAllowed()) {
            log.warn("Usuário não será cadastrado pois não foi validado no rollout.");
            throw new BusinessException( "Usuário não disponivel no rollout", HttpStatus.UNPROCESSABLE_ENTITY);
        }



        userDTO = userMapper.convertToUserDTO(
                repository.save(userMapper.convertToUser(userDTO)));
        log.info("Usuário cadastrado com sucesso! ID: {}", userDTO.getId());

        return userDTO;

    }

    public UserDTO findById(Long id) {
        log.debug("Buscando usuário com ID: {}", id);
        UserDTO userDTO = userMapper.convertToUserDTO(repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Usuário não encontrado")));
        log.debug("Usuário com ID: {} encontrado", id);
        return userDTO;
    }

    public List<UserDTO> findAll() {
        log.debug("Iniciando consulta de usuários...");
        List<UserDTO> userDTOList = repository.findAll().stream().map(userMapper::convertToUserDTO).toList();
        log.info("Consulta finalizada. Total de usuários encontrados: {}", userDTOList.size());
        return userDTOList;
    }

    public UserDTO update( UserDTO userDTO){
        log.debug("Atualizando usuário com ID: {}", userDTO.getId());
        this.findById(userDTO.getId());
        auxVerifyAndPopulateAddress(userDTO);
        UserDTO updated = userMapper.convertToUserDTO(
                repository.save(userMapper.convertToUser(userDTO)));
        log.info("Usuário com ID: {} atualizado com sucesso", updated.getId());
        return updated;

    }

    public void deleteById(Long id) {
        log.debug("Excluindo usuário com ID: {}", id);
        this.findById(id);
        repository.deleteById(id);
        log.debug("Usuário com ID: {} removido com sucesso.", id);
    }

    public void auxVerifyAndPopulateAddress(UserDTO userDTO) {

        List<String> postalCodeList = new ArrayList<>();

        log.info("Validando dados de endereço recebidos na requisição.");
        if (userDTO.getAddressDTOList() == null) {
            log.warn("Deve-se incluir ao menos um endereço na requisiçao.");
            throw new BusinessException("Usuário deve cadastrar ao menos um endereço com CEP e número", HttpStatus.UNPROCESSABLE_ENTITY);
        }

        boolean hasInvalidAddress = userDTO.getAddressDTOList().stream()
                .anyMatch(addressDTO -> addressDTO.getPostalCode() == null || addressDTO.getNumber() == null);

        if (hasInvalidAddress) {
            log.warn("CEP e número do endereço são obrigatórios para o cadastro de novo usuário.");
            throw new BusinessException("CEP e número são obrigatórios para cadastro de endereço.", HttpStatus.UNPROCESSABLE_ENTITY);

        }


        for (int i = 0; i < userDTO.getAddressDTOList().size(); i++) {

            AddressDTO addressDTO = userDTO.getAddressDTOList().get(i);

            String postalCode = addressDTO.getPostalCode().replace(".", "").replace("-", "");

            if (postalCodeList.contains(postalCode)) {
                log.warn("CEP não pode ser repetido no cadastro.");
                throw new BusinessException("CEP não pode ser repetido no cadastro.", HttpStatus.UNPROCESSABLE_ENTITY);
            }

            postalCodeList.add(postalCode);

            try {
                PostalCodeAddressResponse postalCodeAddressResponse =
                        postalCodeAddressPort.fetchAddressByPostalCode(postalCode);
                addressDTO = addressMapper.convertToAddressDtoFromResponse(postalCodeAddressResponse, addressDTO);

                userDTO.getAddressDTOList().set(i, addressDTO);

            } catch (Exception e) {
                log.error("CEP inválido. Erro ao consultar CEP {}: {}", postalCode, e.getMessage(), e);
                throw new BusinessException("CEP inválido. Erro inesperado ao consultar CEP na api OpenCep",
                        HttpStatus.INTERNAL_SERVER_ERROR);
            }


        }


    }



}
