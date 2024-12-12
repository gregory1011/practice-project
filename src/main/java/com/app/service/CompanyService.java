package com.app.service;

import com.app.dto.CompanyDto;
import java.util.List;


public interface CompanyService {

    CompanyDto getCompanyByLoggedInUser();
    CompanyDto findById(Long id);
    List<CompanyDto> listAllCompanies();
    CompanyDto saveCompany(CompanyDto newCompany);
    CompanyDto activateCompany(Long id);
    CompanyDto deactivateCompany(Long id);
    CompanyDto updateCompany(CompanyDto dto);
    boolean titleExist(CompanyDto dto);

}
