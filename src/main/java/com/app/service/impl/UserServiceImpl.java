package com.app.service.impl;

import com.app.dto.UserDto;
import com.app.entity.User;
import com.app.exceptions.UserNotFoundException;
import com.app.repository.UserRepository;
import com.app.service.UserService;
import com.app.util.MapperUtil;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;


@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final MapperUtil mapperUtil;


    @Override
    public UserDto findByUsername(String username) {
        User user = userRepository.findByUsername(username).orElseThrow(()-> new UserNotFoundException(username));
        return mapperUtil.convert(user, new UserDto());
    }


}
