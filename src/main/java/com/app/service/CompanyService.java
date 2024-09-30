package com.app.service;

import com.app.dto.CompanyDto;

public interface CompanyService {

    CompanyDto getCompanyByIdByLoggedInUser(String companyId);
}
