package com.app.service;

import com.app.entity.User;
import com.app.entity.common.UserPrincipal;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextImpl;


public class SecuritySetUpUtil {

    public static void setUpSecurityContext(Long userId, String username){
        User user = new User();
        user.setUsername(username);
        user.setId(userId);
        UserPrincipal userPrincipal = new UserPrincipal(user);
        TestingAuthenticationToken authentication = new TestingAuthenticationToken(userPrincipal, null);
        SecurityContextHolder.setContext(new SecurityContextImpl(authentication));
    }

    public static void setUpSecurityContext(){
        setUpSecurityContext(2L, "manager@greentech.com");
    }

}
