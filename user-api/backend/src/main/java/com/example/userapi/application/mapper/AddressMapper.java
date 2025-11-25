package com.example.userapi.application.mapper;

import com.example.userapi.adapter.out.repository.AddressEntity;
import com.example.userapi.adapter.out.repository.UserEntity;
import com.example.userapi.application.dto.AddressDTO;
import com.example.userapi.application.dto.UserDTO;
import com.example.userapi.application.response.PostalCodeAddressResponse;
import com.example.userapi.domain.model.Address;
import com.example.userapi.domain.model.User;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class AddressMapper {

    public List<AddressDTO> convertToAddressDTO(List<Address> addressList, UserDTO userDTO){

        List<AddressDTO> addressDTOList = new ArrayList<>();

        for(Address address: addressList){
            AddressDTO addressDTO = new AddressDTO();
            addressDTO.setId(address.getId());
            addressDTO.setPostalCode(address.getPostalCode());
            addressDTO.setNumber(address.getNumber());
            addressDTO.setStreet(address.getStreet());
            addressDTO.setCity(address.getCity());
            addressDTO.setComplement(address.getComplement());
            addressDTO.setNeighborhood(address.getNeighborhood());
            addressDTO.setUserId(userDTO.getId());

            addressDTOList.add(addressDTO);
        }
        return  addressDTOList;
    }

    public List<Address> convertToAddressFromDTO(List<AddressDTO> addressDTOList, User user) {

        List<Address> addresses = new ArrayList<>();
        for (AddressDTO addressDTO : addressDTOList) {
            Address address = new Address();

            address.setId(addressDTO.getId());
            address.setPostalCode(addressDTO.getPostalCode());
            address.setNumber(addressDTO.getNumber());
            address.setStreet(addressDTO.getStreet());
            address.setNeighborhood(addressDTO.getNeighborhood());
            address.setCity(addressDTO.getCity());
            address.setComplement(addressDTO.getComplement());
            address.setUser(user);

            addresses.add(address);
        }

        return addresses;
    }


    public List<AddressEntity> convertToAddressEntity(List<Address> addressList, UserEntity userEntity) {
        List<AddressEntity> entities = new ArrayList<>();
        for (Address address : addressList) {
            AddressEntity entity = new AddressEntity();
            entity.setId(address.getId());
            entity.setPostalCode(address.getPostalCode());
            entity.setNumber(address.getNumber());
            entity.setStreet(address.getStreet());
            entity.setNeighborhood(address.getNeighborhood());
            entity.setCity(address.getCity());
            entity.setComplement(address.getComplement());
            entity.setUserEntity(userEntity);
            entities.add(entity);
        }

        return entities;
    }

    public List<Address> convertToAddressFromEntity(List<AddressEntity> addressEntityList, User user) {
        List<Address> addresses = new ArrayList<>();
        for (AddressEntity addressEntity : addressEntityList) {
            Address address = new Address();
            address.setId(addressEntity.getId());

            address.setPostalCode(addressEntity.getPostalCode());
            address.setNumber(addressEntity.getNumber());
            address.setStreet(addressEntity.getStreet());
            address.setNeighborhood(addressEntity.getNeighborhood());
            address.setCity(addressEntity.getCity());
            address.setComplement(addressEntity.getComplement());
            address.setUser(user);

            addresses.add(address);
        }

        return addresses;
    }

    public AddressDTO convertToAddressDtoFromResponse(PostalCodeAddressResponse addressResponse,AddressDTO addressDTO){
        addressDTO.setCity(addressResponse.getLocalidade());
        addressDTO.setNeighborhood(addressResponse.getBairro());
        addressDTO.setStreet(addressResponse.getLogradouro());

        return addressDTO;
    }




}
