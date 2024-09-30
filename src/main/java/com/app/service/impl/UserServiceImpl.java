package com.app.service.impl;

import com.app.entity.User;
import com.app.service.UserService;
import org.springframework.stereotype.Service;


@Service
public class UserServiceImpl implements UserService {


    @Override
    public User findByUsername(String username) {
        return null;
    }


}
