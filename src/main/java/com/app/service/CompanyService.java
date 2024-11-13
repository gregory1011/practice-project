package com.app.service;

import com.app.dto.CompanyDto;
import java.util.List;


public interface CompanyService {

    CompanyDto listCompanyById(Long id);
    CompanyDto getCompanyByLoggedInUser();
    List<CompanyDto> listAllCompanies();
    void saveCompany(CompanyDto newCompany);
    void activateCompany(Long id);
    void deactivateCompany(Long id);
    void updateCompany(Long id, CompanyDto companyDto);
}
