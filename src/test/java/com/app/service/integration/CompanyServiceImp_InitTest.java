package com.app.service.integration;

import com.app.dto.CompanyDto;
import com.app.entity.Company;
import com.app.enums.CompanyStatus;
import com.app.exceptions.CompanyNotFoundException;
import com.app.repository.CompanyRepository;
import com.app.service.CompanyService;
import com.app.service.SecuritySetUpUtil;
import com.app.service.TestDocInitializer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.ThrowableAssert.catchThrowable;
import static org.junit.jupiter.api.Assertions.*;


//@Transactional // transactional is not required since we don't use delete method
@SpringBootTest
public class CompanyServiceImp_InitTest {

    @Autowired
    private CompanyService companyService;
    @Autowired
    private CompanyRepository companyRepository;

    // for security userLogin
    @BeforeEach
    void setUp() {
        SecuritySetUpUtil.setUpSecurityContext();
    }

    @Test
    void test_FindById() {
        CompanyDto dto = companyService.findById(1L);
        assertThat(dto).isNotNull();
        assertThat(dto.getId()).isEqualTo(1L);
    }
    @Test
    void test_FindById_NotFound() {
        Throwable throwable = catchThrowable(() -> companyService.findById(-11L));
        assertInstanceOf(CompanyNotFoundException.class, throwable);
        assertThat(throwable.getMessage()).isEqualTo("Company not found");
    }

    @Test
    void test_getCompanyByLoggedUser() {
        CompanyDto result = companyService.getCompanyByLoggedInUser();
        assertThat(result).isNotNull();
        assertEquals("Green Tech", result.getTitle());
    }

    @Test
    void test_ListAllCompanies() {
        List<CompanyDto> listResult= companyService.listAllCompanies();
        assertNotNull(listResult);
    }

    @Test
    void test_saveCompany() {
        CompanyDto dto = TestDocInitializer.getCompany(CompanyStatus.PASSIVE);
        CompanyDto result = companyService.saveCompany(dto);
        assertNotNull(result);
        assertEquals(dto.getTitle(), result.getTitle());
        assertEquals(CompanyStatus.PASSIVE, result.getCompanyStatus());
    }

    @Test
    void test_ActivateCompany() {
        CompanyDto result = companyService.activateCompany(1L);
        assertNotNull(result);
        assertEquals(CompanyStatus.ACTIVE, result.getCompanyStatus());

        Company company = companyRepository.findById(1L).orElseThrow();
        assertEquals(CompanyStatus.ACTIVE, company.getCompanyStatus());
    }

    @Test
    void test_DeactivateCompany() {
        CompanyDto result = companyService.deactivateCompany(1L);
        assertNotNull(result);
        assertEquals(CompanyStatus.PASSIVE, result.getCompanyStatus());

        Company company = companyRepository.findById(1L).orElseThrow();
        assertEquals(CompanyStatus.PASSIVE, company.getCompanyStatus());
    }

    @Test
    void test_UpdateCompany() {
        CompanyDto dto = companyService.findById(1L);
        dto.setTitle("Updated Title");

        CompanyDto result = companyService.updateCompany(dto);
        assertNotNull(result);
        assertEquals("Updated Title", result.getTitle());
    }

    @Test
    void test_TitleExists() {
        CompanyDto dto = companyService.findById(1L);
        dto.setTitle("Red Tech");
        boolean result = companyService.titleExist(dto);
        assertTrue(result);
    }

    @Test
    void test_TitleNotExists() {
        CompanyDto dto = TestDocInitializer.getCompany(CompanyStatus.ACTIVE);
        boolean result = companyService.titleExist(dto);
        assertFalse(result);
    }

}
