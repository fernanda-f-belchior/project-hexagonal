package com.example.userapi.application.service;

import com.example.userapi.application.dto.AddressDTO;
import com.example.userapi.application.dto.UserDTO;
import com.example.userapi.application.mapper.AddressMapper;
import com.example.userapi.application.mapper.UserMapper;
import com.example.userapi.application.response.PostalCodeAddressResponse;
import com.example.userapi.domain.exception.BusinessException;
import com.example.userapi.domain.model.Address;
import com.example.userapi.domain.model.User;
import com.example.userapi.port.PostalCodeAddressPort;
import com.example.userapi.port.UserRepositoryPort;
import com.example.userapi.port.RolloutValidatorPort;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.http.HttpStatus;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserServiceTest {

    @Mock private UserRepositoryPort repository;
    @Mock private RolloutValidatorPort rolloutValidatorPort;
    @Mock private PostalCodeAddressPort postalCodeAddressPort;
    @Mock private UserMapper userMapper;
    @Mock private AddressMapper addressMapper;

    @InjectMocks
    private UserService service;




    @BeforeEach
    void setUp() {
        repository = mock(UserRepositoryPort.class);
        rolloutValidatorPort = mock(RolloutValidatorPort.class);
        postalCodeAddressPort = mock(PostalCodeAddressPort.class);
        userMapper = mock(UserMapper.class);
        addressMapper = mock(AddressMapper.class);

        service = new UserService(repository, rolloutValidatorPort, postalCodeAddressPort, userMapper,addressMapper);
    }

    @Test
    void save_shouldThrowException_whenNameOrEmailIsNull() {
        UserDTO userDTO = new UserDTO();
        userDTO.setName(null);
        userDTO.setEmail(null);

        BusinessException exception = assertThrows(BusinessException.class, () -> service.save(userDTO));
        assertEquals("Nome e email são obrigatórios", exception.getMessage());
    }

    @Test
    void save_shouldThrowException_whenRolloutValidatorReturnsFalse() {

        AddressDTO addressDTO = new AddressDTO(1L, "01001000", 123L, "", "", "", "", null);
        List<AddressDTO> addressDTOList = new ArrayList<>();
        addressDTOList.add(addressDTO);

        UserDTO userDTO = new UserDTO();
        userDTO.setName("João");
        userDTO.setEmail("joao@example.com");
        userDTO.setAddressDTOList(addressDTOList);

        PostalCodeAddressResponse postalCodeAddressResponse = new PostalCodeAddressResponse(
                "01001-000",
                "Praça da Sé",
               "lado ímpar",
                "Sé",
                "São Paulo",
                "SP",
                "3550308"
        );


        when(postalCodeAddressPort.fetchAddressByPostalCode("01001000")).thenReturn(postalCodeAddressResponse);
        when(rolloutValidatorPort.isRegistrationAllowed()).thenReturn(false);

        BusinessException exception = assertThrows(BusinessException.class, () -> service.save(userDTO));
        assertEquals("Usuário não disponivel no rollout", exception.getMessage());
    }

    @Test
    void save_shouldPersistUser_whenValidatorReturnsTrue() {
        AddressDTO addressDTO = new AddressDTO(1L, "01001000", 123L, "", "", "", "", null);
        Address address = new Address(1L, "01001000", 123L, "", "", "", "", null);
        List<Address> addressList = new ArrayList<>();
        List<AddressDTO> addressDTOList = new ArrayList<>();
        addressList.add(address);
        addressDTOList.add(addressDTO);

        User user = new User();
        user.setId(1L);
        user.setName("Maria");
        user.setEmail("maria@example.com");
        user.setAddressList(addressList);

        UserDTO userDTO = new UserDTO();
        userDTO.setId(1L);
        userDTO.setName("Maria");
        userDTO.setEmail("maria@example.com");
        userDTO.setAddressDTOList(addressDTOList);

        PostalCodeAddressResponse postalCodeAddressResponse = new PostalCodeAddressResponse(
                "01001-000",
                "Praça da Sé",
                "lado ímpar",
                "Sé",
                "São Paulo",
                "SP",
                "3550308"
        );

        User savedUser = new User();
        savedUser.setId(1L);
        savedUser.setName("Maria");
        savedUser.setEmail("maria@example.com");

        when(postalCodeAddressPort.fetchAddressByPostalCode("01001000")).thenReturn(postalCodeAddressResponse);
        when(rolloutValidatorPort.isRegistrationAllowed()).thenReturn(true);
        when(userMapper.convertToUser(userDTO)).thenReturn(user); // converte o DTO para entidade
        when(repository.save(user)).thenReturn(savedUser); // simula salvar e retornar a entidade persistida
        when(userMapper.convertToUserDTO(savedUser)).thenReturn(new UserDTO(1L, "Maria", "maria@example.com", addressDTOList));
        when(repository.save(user)).thenReturn(savedUser);

        UserDTO result = service.save(userDTO);

        assertEquals(1L, result.getId());
        assertEquals("Maria", result.getName());
        verify(repository).save(userMapper.convertToUser(userDTO));
    }


    @Test
    void findById_shouldReturnUser_whenExists() {
        // entidade simulada
        User user = new User();
        user.setId(1L);
        user.setName("Ana");
        user.setEmail("ana@email.com");

        // DTO esperado
        UserDTO userDTO = new UserDTO();
        userDTO.setId(1L);
        userDTO.setName("Ana");
        userDTO.setEmail("ana@email.com");

        when(repository.findById(1L)).thenReturn(Optional.of(user));
        when(userMapper.convertToUserDTO(user)).thenReturn(userDTO);
        UserDTO result = service.findById(1L);

        assertEquals(1L, result.getId());
        assertEquals("Ana", result.getName());
        assertEquals("ana@email.com", result.getEmail());
    }


    @Test
    void findById_shouldThrowEntityNotFoundException_whenUserDoesNotExist() {
        // simula que o repositório não encontrou nada
        when(repository.findById(99L)).thenReturn(Optional.empty());

        // verifica que a exceção é lançada
        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> service.findById(99L));

        // valida a mensagem
        assertEquals("Usuário não encontrado", exception.getMessage());
    }

    @Test
    void findAll_shouldReturnListOfUsers() {
        User firstUser = new User();
        firstUser.setId(1L);
        User secondUser = new User();
        secondUser.setId(2L);
        List<AddressDTO> addressDTOList = new ArrayList<>();

        when(repository.findAll()).thenReturn(Arrays.asList(firstUser, secondUser));
        when(userMapper.convertToUserDTO(firstUser)).thenReturn(new UserDTO(1L, "Alice", "alice@123", addressDTOList));
        when(userMapper.convertToUserDTO(secondUser)).thenReturn(new UserDTO(2L, "Maria", "maria@123", addressDTOList));

        List<UserDTO> result = service.findAll();

        assertEquals(2, result.size());
        assertEquals("Alice", result.get(0).getName());
        assertEquals("Maria", result.get(1).getName());


        List<UserDTO> userList = service.findAll();
        assertEquals(2, userList.size());

    }

    @Test
    void update_shouldUpdateUser() {
        // DTO recebido na requisição com endereço válido
        AddressDTO addressDTO = new AddressDTO(1L, "01001-000", 123L, "", "", "", "", null);
        List<AddressDTO> addressDTOList = new ArrayList<>();
        addressDTOList.add(addressDTO);

        UserDTO userDTO = new UserDTO();
        userDTO.setId(1L);
        userDTO.setEmail("ana@email.com");
        userDTO.setName("Ana");
        userDTO.setAddressDTOList(addressDTOList);

        // entidade existente
        User existingUser = new User();
        existingUser.setId(1L);
        existingUser.setEmail("ana@email.com");
        existingUser.setName("Ana");
        existingUser.setAddressList(new ArrayList<>());

        // entidade após atualização
        User updatedUser = new User();
        updatedUser.setId(1L);
        updatedUser.setEmail("joana@email.com");
        updatedUser.setName("Joana");
        updatedUser.setAddressList(new ArrayList<>());

        // DTO esperado após atualização
        UserDTO updatedDTO = new UserDTO();
        updatedDTO.setId(1L);
        updatedDTO.setEmail("joana@email.com");
        updatedDTO.setName("Joana");
        updatedDTO.setAddressDTOList(addressDTOList);

        // resposta da API de CEP (postalCodeAddressPort)
        PostalCodeAddressResponse postalCodeAddressResponse = new PostalCodeAddressResponse(
                "01001-000",
                "Praça da Sé",
                "lado ímpar",
                "Sé",
                "São Paulo",
                "SP",
                "3550308"
        );

        when(repository.findById(1L)).thenReturn(Optional.of(existingUser));
        when(postalCodeAddressPort.fetchAddressByPostalCode("01001000")).thenReturn(postalCodeAddressResponse);
        when(userMapper.convertToUser(userDTO)).thenReturn(existingUser);
        when(repository.save(existingUser)).thenReturn(updatedUser);
        when(userMapper.convertToUserDTO(updatedUser)).thenReturn(updatedDTO);
        UserDTO result = service.update(userDTO);

        assertEquals(1L, result.getId());
        assertEquals("Joana", result.getName());
        assertEquals("joana@email.com", result.getEmail());

        verify(repository).findById(1L);
        verify(postalCodeAddressPort).fetchAddressByPostalCode("01001000");
        verify(userMapper).convertToUser(userDTO);
        verify(repository).save(existingUser);
        verify(userMapper).convertToUserDTO(updatedUser);

    }

    @Test
    void deleteById_shouldDeleteUser_whenExists() {
        User user = new User();
        user.setId(1L);
        user.setName("Ana");
        user.setEmail("ana@email.com");

        UserDTO userDTO = new UserDTO();
        userDTO.setId(1L);
        userDTO.setName("Ana");
        userDTO.setEmail("ana@email.com");

        when(repository.findById(1L)).thenReturn(Optional.of(user));
        when(userMapper.convertToUserDTO(user)).thenReturn(userDTO);
        service.deleteById(1L);

        verify(repository).findById(1L);
        verify(userMapper).convertToUserDTO(user);
        verify(repository).deleteById(1L);
    }

    @Test
    void deleteById_shouldThrowEntityNotFoundException_whenUserDoesNotExist() {
        // simula que o repositório não encontrou nada
        when(repository.findById(1L)).thenReturn(Optional.empty());

        // verifica que a exceção é lançada
        EntityNotFoundException exception =
                assertThrows(EntityNotFoundException.class, () -> service.deleteById(1L));

        // valida a mensagem definida no UserService
        assertEquals("Usuário não encontrado", exception.getMessage());

        // garante que o delete nunca foi chamado
        verify(repository, never()).deleteById(anyLong());

    }

    @Test
    void auxVerifyAndPopulateAddress_shouldThrowException_whenCepIsRepeated() {
        AddressDTO address1 = new AddressDTO();
        address1.setPostalCode("01001-000");
        address1.setNumber(123L);

        AddressDTO address2 = new AddressDTO();
        address2.setPostalCode("01001-000");
        address2.setNumber(456L);

        List<AddressDTO> addressList = new ArrayList<>();
        addressList.add(address1);
        addressList.add(address2);

        UserDTO userDTO = new UserDTO();
        userDTO.setId(1L);
        userDTO.setName("João");
        userDTO.setEmail("joao@example.com");
        userDTO.setAddressDTOList(addressList);
        PostalCodeAddressResponse postalCodeAddressResponse = new PostalCodeAddressResponse(
                "01001-000",
                "Praça da Sé",
                "lado ímpar",
                "Sé",
                "São Paulo",
                "SP",
                "3550308"
        );

        when(postalCodeAddressPort.fetchAddressByPostalCode("01001000")).thenReturn(postalCodeAddressResponse);
        when(addressMapper.convertToAddressDtoFromResponse(postalCodeAddressResponse, address1)).thenReturn(address1);


        BusinessException exception = assertThrows(
                BusinessException.class,
                () -> service.auxVerifyAndPopulateAddress(userDTO)
        );

        assertEquals("CEP não pode ser repetido no cadastro.", exception.getMessage());
        assertEquals(HttpStatus.UNPROCESSABLE_ENTITY, exception.getStatus());
    }





}