package com.app.service.impl;


import com.app.dto.UserDto;
import com.app.entity.User;
import com.app.entity.common.UserPrincipal;
import com.app.exceptions.UserNotFoundException;
import com.app.repository.UserRepository;
import com.app.service.SecurityService;
import com.app.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class SecurityServiceImpl implements SecurityService {


    private final UserRepository userRepository;
    public final UserService userService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username).orElseThrow(UserNotFoundException::new);
        return new UserPrincipal(user);
    }

    @Override
    public UserDto getLoggedInUser() {
        var username = SecurityContextHolder.getContext().getAuthentication().getName();
        return userService.findByUsername(username);
    }

}
