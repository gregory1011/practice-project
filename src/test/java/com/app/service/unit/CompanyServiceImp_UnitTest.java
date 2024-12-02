package com.app.service.unit;


import com.app.dto.CompanyDto;
import com.app.dto.UserDto;
import com.app.entity.Company;
import com.app.entity.Role;
import com.app.entity.User;
import com.app.enums.CompanyStatus;
import com.app.exceptions.CompanyNotFoundException;
import com.app.repository.CompanyRepository;
import com.app.service.SecurityService;
import com.app.service.TestDocInitializer;
import com.app.service.impl.CompanyServiceImpl;
import com.app.util.MapperUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.ThrowableAssert.catchThrowable;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
public class CompanyServiceImp_UnitTest {

    @Mock
    private CompanyRepository companyRepository;
    @Spy
    private MapperUtil mapperUtil= new MapperUtil(new ModelMapper());
    @Mock
    private SecurityService securityService;
    @InjectMocks
    private CompanyServiceImpl companyService;

    private Company company;
    private CompanyDto companyDto;
    private User user;

    @BeforeEach
    void setUp() {
        company= new Company();
        company.setId(1L);
        company.setTitle("Test_Company");
        company.setCompanyStatus(CompanyStatus.ACTIVE);

        companyDto= new CompanyDto();
        companyDto.setId(1L);
        companyDto.setTitle("Test_Company");
        companyDto.setCompanyStatus(CompanyStatus.ACTIVE);

        Role role= new Role();
        role.setId(1L);
        role.setDescription("Admin");
        user= new User();
        user.setRole(role);
        user.setCompany(company);

    }

    @Test
    void getCompanyByLoggedInUser_shouldReturnCompanyDto(){
        //given
        CompanyDto expectedResult = TestDocInitializer.getCompany(CompanyStatus.ACTIVE);
        UserDto loggedInUser = TestDocInitializer.getUser("Admin");

        //when
        when(securityService.getLoggedInUser()).thenReturn(loggedInUser);

        CompanyDto actualResult = companyService.getCompanyByLoggedInUser();

        assertThat(actualResult).usingRecursiveComparison().isEqualTo(expectedResult);
        verify(securityService, times(1)).getLoggedInUser();
    }

    @Test
    void findById_shouldReturnCompanyDto() {
        //given
        CompanyDto dto = TestDocInitializer.getCompany(CompanyStatus.ACTIVE);
        Company entity = mapperUtil.convert(dto, new Company());
        // when part -> mock repository
        when(companyRepository.findById(1L)).thenReturn(Optional.of(entity));
        //calling the actual method
        CompanyDto result = companyService.findById(1L);
        //then
        assertThat(result).usingRecursiveComparison().isEqualTo(dto);
        verify(companyRepository, times(1)).findById(1L);
    }
    @Test
    void findById_shouldThrowException() {
        // when part
        when(companyRepository.findById(1L)).thenReturn(Optional.empty());
        Throwable throwable = catchThrowable(() -> companyService.findById(1L));
        //then part
        assertThat(throwable).isInstanceOf(CompanyNotFoundException.class);
        verify(companyRepository, times(1)).findById(1L);
        assertThat(throwable.getMessage()).isEqualTo("Company not found");
    }
}


