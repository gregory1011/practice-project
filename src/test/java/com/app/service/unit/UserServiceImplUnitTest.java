package com.app.service.unit;


import com.app.dto.CompanyDto;
import com.app.dto.UserDto;
import com.app.entity.User;
import com.app.enums.CompanyStatus;
import com.app.exceptions.UserNotFoundException;
import com.app.repository.UserRepository;
import com.app.service.SecurityService;
import com.app.service.TestDocInitializer;
import com.app.service.impl.UserServiceImpl;
import com.app.util.MapperUtil;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;


import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.ThrowableAssert.catchThrowable;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.params.provider.Arguments.arguments;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;



@ExtendWith(MockitoExtension.class)
public class UserServiceImplUnitTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private SecurityService securityService;
    @Spy //we use @spy because we want to create an instance of this ModelMapper class and initialize if.
    private MapperUtil mapperUtil= new MapperUtil(new ModelMapper());
    @Mock
    private PasswordEncoder passwordEncoder;
    @InjectMocks
    private UserServiceImpl userService;


    @Test
    void shouldFindUserById_thenReturn_USER_ifExists(){
        // given
        UserDto userDto = TestDocInitializer.getUser("Admin");
        CompanyDto company = TestDocInitializer.getCompany(CompanyStatus.ACTIVE);
        userDto.setCompany(company);
        User user = mapperUtil.convert(userDto, new User());
        //when
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
        when(userRepository.countAllByCompany_IdAndRoleIsAdmin(anyLong())).thenReturn(1);

        UserDto actualUser = userService.listById(1L);
        assertThat(actualUser.isOnlyAdmin()).isTrue();
        assertThat(actualUser).usingRecursiveComparison()
                .ignoringFields("password", "confirmPassword", "isOnlyAdmin")
                .isEqualTo(userDto);
    }
    @Test
    void shouldFindUserById_thenThrowException_ifNotExists(){
        when(userRepository.findById(anyLong())).thenReturn(Optional.empty());
        Throwable throwable = catchThrowable(() -> userService.listById(1L));
        //then assert
        assertThat(throwable).isInstanceOf(UserNotFoundException.class);
        assertThat(throwable.getMessage()).isEqualTo("User not found");

    }

    @Test
    void shouldFindUserByUsername_thenReturn_USER_IfExists() {
        //given
        UserDto userDto = TestDocInitializer.getUser("Manager");
        User user = mapperUtil.convert(userDto, new User());

        // when
        when(userRepository.findByUsername(anyString())).thenReturn(Optional.of(user));
        UserDto actualUser = userService.findByUsername(userDto.getUsername());

        // then assert
        assertThat(actualUser).usingRecursiveComparison()
                .ignoringFields("password", "confirmPassword")
                .isEqualTo(userDto);
    }
    @Test
    void shouldFindUserByUsername_then_ThrowException_IfNotExists() {
        // when user if not present, returns empty or null
        when(userRepository.findByUsername(anyString())).thenReturn(Optional.empty());
        Throwable throwable = catchThrowable(() -> userService.findByUsername(anyString())); // catch exception when use this method
        assertThat(throwable).isInstanceOf(UserNotFoundException.class);
        assertThat(throwable.getMessage()).isEqualTo("User not found");
    }

    @Test
    void shouldReturn_sortedUserList_forRootUser(){
        //given a list of Users
        List<UserDto> dtoList= Arrays.asList(
                TestDocInitializer.getUser("Root User"),
                TestDocInitializer.getUser("Admin"),
                TestDocInitializer.getUser("Admin"),
                TestDocInitializer.getUser("Admin"));
        dtoList.get(0).getCompany().setTitle("XY");
        dtoList.get(1).getCompany().setTitle("ECV");
        dtoList.get(2).getCompany().setTitle("VBN");
        dtoList.get(3).getCompany().setTitle("OME");

        List<User> userList = dtoList.stream()
                .map(each -> mapperUtil.convert(each, new User())).toList();
        List<UserDto> expectedList = dtoList.stream()
                .sorted(Comparator.comparing((UserDto u) -> u.getCompany().getTitle())
                        .thenComparing(m -> m.getRole().getDescription())).toList();

        // when part
        when(securityService.getLoggedInUser()).thenReturn(dtoList.get(0)); // return RootUser
        when(userRepository.findAllByRole_id(anyLong())).thenReturn(userList);

        //then
        List<UserDto> actualList = userService.listAllUsers();
        //assert
        assertThat(userList).hasSize(4);
        assertThat(actualList).usingRecursiveComparison()
                .ignoringFields("password", "confirmPassword")
                .isEqualTo(expectedList);


    }
    @Test
    void shouldReturn_sortedUserList_forAdmin(){
        //given a list of Users
        CompanyDto company = TestDocInitializer.getCompany(CompanyStatus.ACTIVE);

        List<UserDto> dtoList= Arrays.asList(
                TestDocInitializer.getUser("Admin"),
                TestDocInitializer.getUser("Admin"),
                TestDocInitializer.getUser("Manger"),
                TestDocInitializer.getUser("Employee"));
        dtoList.get(0).getCompany().setTitle("XY");
        dtoList.get(0).setCompany(company);
        dtoList.get(0).setOnlyAdmin(true);
        dtoList.get(1).getCompany().setTitle("ECV");
        dtoList.get(1).setCompany(company);
        dtoList.get(1).setOnlyAdmin(false);
        dtoList.get(2).getCompany().setTitle("VBN");
        dtoList.get(2).setCompany(company);
        dtoList.get(3).getCompany().setTitle("OME");
        dtoList.get(3).setCompany(company);

        List<User> userList = dtoList.stream()
                .map(each -> mapperUtil.convert(each, new User())).toList();
        List<UserDto> expectedList = dtoList.stream()
                .sorted(Comparator.comparing((UserDto u) -> u.getCompany().getTitle())
                        .thenComparing(m -> m.getRole().getDescription())).toList();

        //when
        when(securityService.getLoggedInUser()).thenReturn(dtoList.get(0));
        when(userRepository.findAllByRole_id(anyLong())).thenReturn(userList);
        when(userRepository.countAllByCompany_IdAndRoleIsAdmin(anyLong())).thenReturn(1,2);

        List<UserDto> actualList = userService.listAllUsers();
//        actualList.forEach(each -> System.out.println(each.isOnlyAdmin()));

        //then
        assertThat(userList).hasSize(4);
        assertThat(actualList.get(0).isOnlyAdmin()).isTrue();
        assertThat(actualList.get(1).isOnlyAdmin()).isFalse();
        assertThat(actualList).usingRecursiveComparison()
                .ignoringFields("password", "confirmPassword")
                .isEqualTo(expectedList);

    }

    @Test
    void shouldSaveUser_thenReturnNewUser(){
        UserDto userDto = TestDocInitializer.getUser("Manager");
        userDto.getCompany().setCompanyStatus(CompanyStatus.ACTIVE);
        User user = mapperUtil.convert(userDto, new User());

        //when part
        when(passwordEncoder.encode(anyString())).thenReturn(user.getPassword());
        when(userRepository.save(any(User.class))).thenReturn(user);
        when(securityService.getLoggedInUser()).thenReturn(userDto);

        //then
        UserDto saveUser = userService.saveUser(userDto);
        assertThat(saveUser).isNotNull();
        verify(passwordEncoder).encode(anyString());
        assertThat(userDto.getPassword()).isEqualTo(user.getPassword());
        assertThat(saveUser).usingRecursiveComparison()
                .ignoringFields("password", "confirmPassword")
                .isEqualTo(userDto);
    }

    @Test
    void shouldUpdateUser_thenReturnNewUser(){
        UserDto userDto = TestDocInitializer.getUser("Admin");
        userDto.getCompany().setCompanyStatus(CompanyStatus.ACTIVE);
        userDto.setUsername("jora@cydeo.com");
        userDto.setFirstname("John");

        User user = mapperUtil.convert(userDto, new User());

        //when part
        when(userRepository.findById(anyLong())).thenReturn(Optional.ofNullable(user));
        when(userRepository.save(any(User.class))).thenReturn(user);

        UserDto updatedUser = userService.updateUser(userDto);
        //assert
        verify(passwordEncoder).encode(anyString());
        assertThat(updatedUser).isNotNull();
        assertThat(updatedUser).usingRecursiveComparison()
                .ignoringFields("password", "confirmPassword")
                .isEqualTo(userDto);
    }

    @Test
    void shouldSoftDeleteUser(){
        UserDto userDto = TestDocInitializer.getUser("Employee");
        userDto.getCompany().setCompanyStatus(CompanyStatus.ACTIVE);
        User user = mapperUtil.convert(userDto, new User());
        user.setIsDeleted(false);

        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
        userService.deleteUser(user.getId());

        verify(userRepository, times(1)).save(user);
        assertThat(user.getIsDeleted()).isTrue();
        assertNotEquals(userDto.getUsername(), user.getUsername());
    }
    @Test
    void shouldThrowExceptionWhenIfUserToDeleteDoesNotExist(){
        when(userRepository.findById(anyLong())).thenReturn(Optional.empty());
        Throwable throwable = catchThrowable(() -> userService.deleteUser(1L));
        assertThat(throwable).isInstanceOf(UserNotFoundException.class);
        assertThat(throwable.getMessage()).isEqualTo("User not found");
    }

    @ParameterizedTest()
    @MethodSource(value = "email")
    void shouldCheckIfUserExists_thenReturnTrue(UserDto userDto, User user, boolean expected){
        //when
        when(userRepository.findByUsername(anyString())).thenReturn(Optional.ofNullable(user));
        //then
        assertEquals(expected, userService.isUsernameExists(userDto));
    }
    static Stream<Arguments> email() {
        //given
        UserDto userDto = TestDocInitializer.getUser("Admin");
        User user = new MapperUtil(new ModelMapper()).convert(userDto, new User());
        user.setId(2L);
        return Stream.of(
                arguments(userDto, null, false), // this is first param
                arguments(userDto, user, true)   // 2nd param
        );
    }

}
