package com.app.service.impl;

import com.app.dto.UserDto;
import com.app.entity.User;
import com.app.exceptions.UserNotFoundException;
import com.app.repository.UserRepository;
import com.app.service.UserService;
import com.app.util.MapperUtil;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;


@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final MapperUtil mapperUtil;


    @Override
    public UserDto listById(Long id) {
        User user = userRepository.findById(id).orElseThrow(() -> new UserNotFoundException("User Not Found"));
        return mapperUtil.convert(user, new UserDto());
    }

    @Override
    public UserDto findByUsername(String username) {
        User user = userRepository.findByUsername(username).orElseThrow(()-> new UserNotFoundException(username));
        return mapperUtil.convert(user, new UserDto());
    }

    @Override
    public List<UserDto> listAllUsers() {
        List<User> list = userRepository.findAll();
        return list.stream().map(each -> mapperUtil.convert(each, new UserDto())).toList();
    }

    @Override
    public void saveUser(UserDto userDto) {

        User user = mapperUtil.convert(userDto, new User());
        user.setEnabled(true);
        userRepository.save(user);
    }


}
