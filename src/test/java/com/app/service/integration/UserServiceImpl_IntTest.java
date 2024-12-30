package com.app.service.integration;


import com.app.dto.UserDto;
import com.app.entity.User;
import com.app.exceptions.UserNotFoundException;
import com.app.repository.UserRepository;
import com.app.service.SecuritySetUpUtil;
import com.app.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.ThrowableAssert.catchThrowable;
import static org.junit.jupiter.api.Assertions.*;

@Transactional(readOnly = true) // we use transactional to handle deleteUser() -> when the user: isDeleted= true, and we search for id (in entity class @Where(clause = "is_deleted=false")
@SpringBootTest
public class UserServiceImpl_IntTest {

    @Autowired private UserRepository userRepository;
    @Autowired private UserService userService;
    @Autowired private PasswordEncoder passwordEncoder;


    @BeforeEach
    void setUserService(){
        SecuritySetUpUtil.setUpSecurityContext();
    }

    @Test
    void testFindById(){
        Long id= 1L;
        UserDto userDto = userService.getById(id);
        assertThat(userDto).isNotNull();
        assertThat(userDto.getId()).isEqualTo(id);
    }
    @Test
    void testFindById_notFound(){
        Throwable throwable = catchThrowable(() -> userService.getById(0L));
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

    @Test
    void testListAllUsers(){
        List<UserDto> dtoList = userService.listAllUsers();
        assertThat(dtoList).isNotNull();
        assertFalse(dtoList.isEmpty());

        UserDto userDto = userService.findByUsername("admin@greentech.com");
        assertThat(dtoList.get(0)).usingRecursiveComparison()
                .ignoringFields("onlyAdmin")
                .isEqualTo(userDto);
    }

    @Test
    void testSaveUser(){
//        UserDto userDto = TestDocInitializer.getUser("Admin");
        UserDto userDto = new UserDto();
        userDto.setUsername("test@greentech.com");
        userDto.setPassword(passwordEncoder.encode("test"));

        UserDto savedUserDto = userService.saveUser(userDto);
        assertNotNull(savedUserDto);
        assertEquals(savedUserDto.getUsername(), userDto.getUsername());
        assertEquals(savedUserDto.getPassword(), userDto.getPassword());
    }

    @Test
    void updateUser(){
        UserDto userDto = userService.getById(2L);
        userDto.setUsername("test@greentech.com");
        userDto.setPassword("test");

        UserDto updatedUser = userService.updateUser(userDto);
        assertNotNull(updatedUser);
        assertEquals(updatedUser.getUsername(), userDto.getUsername());
        assertTrue(passwordEncoder.matches(userDto.getPassword(), updatedUser.getPassword()));
    }

    @Test
    void deleteUser(){
        Long id= 2L;
        userService.deleteUser(id);
        User user = userRepository.findById(id).orElseThrow(UserNotFoundException::new);
        assertTrue(user.getIsDeleted());
        assertEquals("admin@greentech.com-"+id, user.getUsername());
    }

    @Test
    void testIsUsernameExist(){
        UserDto userDto = userService.findByUsername("admin@greentech.com");
        userDto.setUsername("manager@greentech.com"); // this username already exist in DB
        assertTrue(userService.isUsernameExists(userDto));
    }
    @Test
    void testIsUsernameExist_notExist(){
        UserDto userDto= new UserDto();
        userDto.setUsername("test@greentech.com");
        assertFalse(userService.isUsernameExists(userDto));
    }

}
