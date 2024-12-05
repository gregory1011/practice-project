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

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.ThrowableAssert.catchThrowable;
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
        company.setTitle("Test Company");
        company.setCompanyStatus(CompanyStatus.ACTIVE);

        companyDto= new CompanyDto();
        companyDto.setId(1L);
        companyDto.setTitle("Test Company");
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

    @Test
    void listAllCompanies_shouldReturnListOfCompanyDto() {
        //given part
        UserDto loggedInUser= mapperUtil.convert(user, new UserDto());

        //when part
        when(securityService.getLoggedInUser()).thenReturn(loggedInUser);
        when(companyRepository.findAll()).thenReturn(List.of(company));

        List<CompanyDto> result = companyService.listAllCompanies();

        //then part
        assertThat(result).usingRecursiveComparison().isEqualTo(List.of(companyDto));
        verify(companyRepository, times(1)).findAll();
        assertThat(result).hasSize(1);
    }

    @Test
    void saveCompany_shouldReturnCompanyDto() {
        //given
        company.setCompanyStatus(CompanyStatus.PASSIVE);
        //when part
        when(companyRepository.save(any(Company.class))).thenReturn(company);

        CompanyDto result = companyService.saveCompany(companyDto);
        //then part
        assertThat(result).usingRecursiveComparison().isEqualTo(companyDto);
    }

    @Test
    void activateCompany_shouldReturnActivatedCompanyDto() {
        //when part
        when(companyRepository.findById(anyLong())).thenReturn(Optional.of(company));
        when(companyRepository.save(any(Company.class))).thenReturn(company);

        CompanyDto result = companyService.activateCompany(1L);
        //then part
        assertThat(result).usingRecursiveComparison().isEqualTo(companyDto);
        assertThat(result.getCompanyStatus()).isEqualTo(CompanyStatus.ACTIVE);
    }

    @Test
    void deactivateCompany_shouldReturnPassiveCompanyDto() {
        //given
        companyDto.setCompanyStatus(CompanyStatus.PASSIVE);
        //when
        when(companyRepository.findById(anyLong())).thenReturn(Optional.of(company));
        when(companyRepository.save(any(Company.class))).thenReturn(company);

        CompanyDto result = companyService.deactivateCompany(1L);

        //then part
        assertThat(result).usingRecursiveComparison().isEqualTo(companyDto);
        assertThat(result.getCompanyStatus()).isEqualTo(CompanyStatus.PASSIVE);
    }

    @Test
    void updateCompany_shouldReturnUpdatedCompany() {
        //when
        when(companyRepository.findById(anyLong())).thenReturn(Optional.of(company));
        when(companyRepository.save(any(Company.class))).thenReturn(company);

        CompanyDto result = companyService.updateCompany(companyDto);
        //then part
        assertThat(result).usingRecursiveComparison().isEqualTo(companyDto);
    }

    @Test
    void titleExist_shouldReturnTrueIfTitleExists() {
        //given
        company.setId(2L);
        //when
        when(companyRepository.findByTitle(anyString())).thenReturn(Optional.of(company));
        boolean result = companyService.titleExist(companyDto);
        assertThat(result).isTrue();
    }
    @Test
    void titleExist_shouldReturnFalseIfTitleDoesNotExist() {
        //when
        when(companyRepository.findByTitle(anyString())).thenReturn(Optional.empty());
        boolean result = companyService.titleExist(companyDto);
        assertThat(result).isFalse();
    }

}


