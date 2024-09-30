package com.app.service.impl;

import com.app.dto.UserDto;
import com.app.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;


@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {


    @Override
    public UserDto findByUsername(String username) {
        return null;
    }


}
