package com.app.service.impl;


import com.app.entity.User;
import com.app.entity.common.UserPrincipal;
import com.app.repository.UserRepository;
import com.app.service.SecurityService;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class SecurityServiceImpl implements SecurityService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException(username));
        if (user==null) throw new UsernameNotFoundException("This "+ username +" does not exist");
        return new UserPrincipal(user);
    }

}
