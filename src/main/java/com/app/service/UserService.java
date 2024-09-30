package com.app.service;

import com.app.dto.UserDto;


public interface UserService {

    UserDto findByUsername(String username);
}
