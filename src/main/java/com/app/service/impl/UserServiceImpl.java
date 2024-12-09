package com.app.service.impl;

import com.app.dto.UserDto;
import com.app.entity.User;
import com.app.enums.CompanyStatus;
import com.app.exceptions.UserNotFoundException;
import com.app.repository.UserRepository;
import com.app.service.SecurityService;
import com.app.service.UserService;
import com.app.util.MapperUtil;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;


@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final MapperUtil mapperUtil;
    private final PasswordEncoder passwordEncoder;
    private final SecurityService securityService;

    public UserServiceImpl(@Lazy SecurityService securityService, UserRepository userRepository, MapperUtil mapperUtil, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.mapperUtil = mapperUtil;
        this.passwordEncoder = passwordEncoder;
        this.securityService = securityService;
    }


    @Override
    public UserDto getById(Long id) {
        User user = userRepository.findById(id).orElseThrow(UserNotFoundException::new);
        UserDto dto = mapperUtil.convert(user, new UserDto());
        dto.setOnlyAdmin(dto.getRole().getDescription().equals("Admin") && this.checkIfOnlyAdmin(dto));
        return dto;
    }

    @Override
    public UserDto findByUsername(String username) {
        User user = userRepository.findByUsername(username).orElseThrow(UserNotFoundException::new);
        return mapperUtil.convert(user, new UserDto());
    }

    @Override
    public List<UserDto> listAllUsers() {
        UserDto loggedInUser = securityService.getLoggedInUser();
        List<User> userList;
        if (loggedInUser.getRole().getId() == 1L){ // if is Root user
            userList= userRepository.findAllByRole_id(2L); // retrieve user id= 2 only Admin
        }else { // retrieve all users by company id
            userList= userRepository.findAllByCompany_id(loggedInUser.getCompany().getId());
        }
        return userList.stream()
                .sorted(Comparator.comparing((User u) -> u.getCompany().getTitle())
                        .thenComparing(u->u.getRole().getDescription()))
                .map(each -> {
                    UserDto dto = mapperUtil.convert(each, new UserDto());
                    dto.setOnlyAdmin(dto.getRole().getDescription().equals("Admin") && this.checkIfOnlyAdmin(dto));
                    return dto;
                }).toList();
//        List<User> list = userRepository.findAll();
//        String username = SecurityContextHolder.getContext().getAuthentication().getName();
//        UserDto user = findByUsername(username);
//        List<UserDto> users= new ArrayList<>();
//        if(user.getRole().getDescription().equals("Root User")){
//            users= list.stream().filter(each -> each.getRole().getDescription().equals("Admin")).map(each ->mapperUtil.convert(each, new UserDto())).toList();
//        } else if(user.getRole().getDescription().equals("Admin")){
//            users= list.stream().filter(u->u.getCompany().getId().equals(user.getCompany().getId())).map(each ->mapperUtil.convert(each, new UserDto())).toList();
//        }
//        return users;
    }

    @Override
    public UserDto saveUser(UserDto userDto) {
        userDto.setPassword(passwordEncoder.encode(userDto.getPassword()));
        User user = mapperUtil.convert(userDto, new User());
        user.setEnabled(securityService.getLoggedInUser().getCompany().getCompanyStatus().equals(CompanyStatus.ACTIVE));
        return mapperUtil.convert(userRepository.save(user), new UserDto());
    }

    @Override
    public UserDto updateUser( UserDto dto) {
        User saved = userRepository.findById(dto.getId()).orElseThrow(UserNotFoundException::new);
        User user = mapperUtil.convert(dto, new User());
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setEnabled(saved.isEnabled());
        return mapperUtil.convert(userRepository.save(user), new UserDto());
    }

    @Override
    public void deleteUser(Long id) {
        User user = userRepository.findById(id).orElseThrow(UserNotFoundException::new);
        user.setIsDeleted(true);
        user.setUsername(user.getUsername()+"-"+user.getId());
        userRepository.save(user);
    }

    @Override
    public boolean isUsernameExists(UserDto userDto) {
        User user = userRepository.findByUsername(userDto.getUsername()).orElse(null);
        if (user == null) return false;
        return !Objects.equals(userDto.getId(), user.getId());
    }

    private boolean checkIfOnlyAdmin(UserDto dto) {
        Integer countAdmins = userRepository.countAllByCompany_IdAndRoleIsAdmin(dto.getCompany().getId());
        return countAdmins == 1;
    }


}
