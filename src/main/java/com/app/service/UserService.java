package com.app.service;

import com.app.dto.UserDto;

import java.util.List;


public interface UserService {

    UserDto getById(Long id);
    UserDto findByUsername(String username);
    List<UserDto> listAllUsers();
    UserDto saveUser(UserDto userDto);
    UserDto updateUser( UserDto user);
    void deleteUser(Long id);
    boolean isUsernameExists(UserDto userDto);

}
