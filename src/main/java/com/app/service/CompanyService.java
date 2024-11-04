package com.app.service;

import com.app.dto.CompanyDto;
import java.util.List;


public interface CompanyService {

    CompanyDto getCompanyById(Long id);
    Long getCompanyIdByLoggedInUser(String companyId);
    List<CompanyDto> listAllCompanies();
    void saveCompany(CompanyDto newCompany);
    void activateCompany(Long id);
    void deactivateCompany(Long id);
    void updateCompany(Long id, CompanyDto companyDto);
}
