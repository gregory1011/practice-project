package com.app.service.impl;

import com.app.dto.UserDto;
import com.app.entity.User;
import com.app.exceptions.UserNotFoundException;
import com.app.repository.UserRepository;
import com.app.service.SecurityService;
import com.app.service.UserService;
import com.app.util.MapperUtil;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final MapperUtil mapperUtil;
    private final SecurityService securityService;

    public UserServiceImpl(UserRepository userRepository, MapperUtil mapperUtil, @Lazy SecurityService securityService) {
        this.userRepository = userRepository;
        this.mapperUtil = mapperUtil;
        this.securityService = securityService;
    }


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
        UserDto user = securityService.getLoggedInUser();
        List<UserDto> users= new ArrayList<>();
        if(user.getRole().getDescription().equals("Root User")){
            users= list.stream().filter(each -> each.getRole().getDescription().equals("Admin")).map(each ->mapperUtil.convert(each, new UserDto())).toList();
        } else if(user.getRole().getDescription().equals("Admin")){
            users= list.stream().filter(u->u.getCompany().getId().equals(user.getCompany().getId())).map(each ->mapperUtil.convert(each, new UserDto())).toList();
        }
        return users;
    }

    @Override
    public void saveUser(UserDto userDto) {
        User user = mapperUtil.convert(userDto, new User());
        user.setEnabled(true);
        userRepository.save(user);
    }

    @Override
    public void updateUser(Long id, UserDto user) {
        User user1 = userRepository.findById(id).orElseThrow(() -> new UserNotFoundException("User Not Found"));
        User user2 = mapperUtil.convert(user, new User());
        user2.setId(user1.getId());
        user2.setEnabled(true);
        userRepository.save(user2);
    }


}
