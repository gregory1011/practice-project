package com.app.service;

import com.app.dto.UserDto;

import java.util.List;


public interface UserService {

    UserDto listById(Long id);
    UserDto findByUsername(String username);
    List<UserDto> listAllUsers();
    void saveUser(UserDto userDto);
    void updateUser(Long id, UserDto user);
}
