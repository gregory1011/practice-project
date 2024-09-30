package com.app.service.impl;


import com.app.dto.CompanyDto;
import com.app.service.CompanyService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class CompanyServiceImpl implements CompanyService {

    @Override
    public CompanyDto getCompanyByIdByLoggedInUser(String companyId) {
        return null;
    }
}
