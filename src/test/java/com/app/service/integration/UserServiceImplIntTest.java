package com.app.service.integration;


import com.app.dto.UserDto;
import com.app.exceptions.UserNotFoundException;
import com.app.repository.UserRepository;
import com.app.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.ThrowableAssert.catchThrowable;
import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
public class UserServiceImplIntTest {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserService userService;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Test
    void testFindById(){
        Long id= 1L;
        UserDto userDto = userService.listById(id);
        assertThat(userDto).isNotNull();
        assertThat(userDto.getId()).isEqualTo(id);
    }
    @Test
    void testFindById_notFound(){
        Throwable throwable = catchThrowable(() -> userService.listById(0L));
        assertThat(throwable).isInstanceOf(UserNotFoundException.class);
        assertThat(throwable.getMessage()).isEqualTo("User not found");
    }

    @Test
    void testFindByUsername(){
        String username = "admin@greentech.com";
        UserDto userDto = userService.findByUsername(username);
        assertNotNull(userDto);
        assertEquals(username, userDto.getUsername());
    }
    @Test
    void testFindByUsername_notFound(){
        Throwable throwable = catchThrowable(() -> userService.findByUsername("test@greentech.com"));
        assertThat(throwable).isInstanceOf(UserNotFoundException.class);
        assertThat(throwable.getMessage()).isEqualTo("User not found");
    }
}
