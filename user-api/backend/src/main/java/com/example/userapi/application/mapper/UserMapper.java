package com.example.userapi.application.mapper;

import com.example.userapi.adapter.out.repository.UserEntity;
import com.example.userapi.application.dto.UserDTO;
import com.example.userapi.domain.model.User;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class UserMapper {

    public UserMapper() {
    }

    private final AddressMapper addressMapper = new AddressMapper();


    public UserEntity convertToUserEntity(User user) {
        if (user == null) { return null;}
        UserEntity userEntity =  new UserEntity();

        userEntity.setId(user.getId());
        userEntity.setName(user.getName());
        userEntity.setEmail(user.getEmail());
        userEntity.setAddressList(
                addressMapper.convertToAddressEntity(user.getAddressList(), userEntity));

        return userEntity;
    }

    public User convertToUser(UserEntity userEntity) {
        if (userEntity == null) { return null;}
        User user =  new User();

        user.setId(userEntity.getId());
        user.setName(userEntity.getName());
        user.setEmail(userEntity.getEmail());
        user.setAddressList(
                addressMapper.convertToAddressFromEntity(userEntity.getAddressList(),user));

        return user;

    }

    public User convertToUser(UserDTO userDTO) {
        if (userDTO == null) { return null;}
        User user =  new User();

        user.setId(userDTO.getId());
        user.setName(userDTO.getName());
        user.setEmail(userDTO.getEmail());
        user.setAddressList(
                addressMapper.convertToAddressFromDTO(userDTO.getAddressDTOList(), user));

        return user;

    }
    public UserDTO convertToUserDTO(User user) {
        if (user == null) { return null;}
        UserDTO userDTO = new UserDTO();
        userDTO.setId(user.getId());
        userDTO.setName(user.getName());
        userDTO.setEmail(user.getEmail());
        userDTO.setAddressDTOList(addressMapper.convertToAddressDTO(user.getAddressList(),userDTO));

        return userDTO;
    }


}
