package com.example.userapi.application.usecases;

import com.example.userapi.application.dto.UserDTO;
import java.util.List;

public interface UserUseCase {

    public UserDTO save(UserDTO userDTO);
    public UserDTO update(UserDTO userDTO);
    public void deleteById(Long id);
    public UserDTO findById(Long id);
    public List<UserDTO> findAll();

}
