package com.app.service;

import com.app.dto.CompanyDto;
import java.util.List;


public interface CompanyService {

    CompanyDto getCompanyByLoggedInUser();
    CompanyDto findById(Long id);
    List<CompanyDto> listAllCompanies();
    void saveCompany(CompanyDto newCompany);
    void activateCompany(Long id);
    void deactivateCompany(Long id);
    void updateCompany(Long id, CompanyDto companyDto);
    boolean titleExist(CompanyDto dto);
}
