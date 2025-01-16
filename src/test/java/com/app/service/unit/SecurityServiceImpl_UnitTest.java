package com.app.service.unit;


import com.app.dto.UserDto;
import com.app.entity.User;
import com.app.entity.common.UserPrincipal;
import com.app.repository.UserRepository;
import com.app.service.TestDocInitializer;
import com.app.service.UserService;
import com.app.service.impl.SecurityServiceImpl;
import com.app.util.MapperUtil;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.ThrowableAssert.catchThrowable;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.params.provider.Arguments.arguments;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
public class SecurityServiceImpl_UnitTest {

    @Mock private UserRepository userRepository;
    @Mock private UserService userService;
    @Spy private MapperUtil mapperUtil= new MapperUtil(new ModelMapper());
    @InjectMocks
    private SecurityServiceImpl securityService;


    @Test
    void test_loadUserByUsername_shouldReturn_userPrincipal() {
        //given
        UserDto userDto = TestDocInitializer.getUser("Admin");
        User user = mapperUtil.convert(userDto, new User());

        //when part
        when(userRepository.findByUsername(anyString())).thenReturn(Optional.of(user));

    }
}
