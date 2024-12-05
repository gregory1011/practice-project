package com.app.service.impl;


import com.app.dto.CompanyDto;
import com.app.dto.UserDto;
import com.app.entity.Company;
import com.app.enums.CompanyStatus;
import com.app.exceptions.CompanyNotFoundException;
import com.app.repository.CompanyRepository;
import com.app.service.CompanyService;
import com.app.service.SecurityService;
import com.app.util.MapperUtil;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;



@Service
@AllArgsConstructor
public class CompanyServiceImpl implements CompanyService {

    private final CompanyRepository companyRepository;
    private final MapperUtil mapperUtil;
    private final SecurityService securityService;


    @Override
    public CompanyDto getCompanyByLoggedInUser() {
        return securityService.getLoggedInUser().getCompany();
    }

    @Override
    public CompanyDto findById(Long id) {
        Company company = companyRepository.findById(id).orElseThrow(() -> new CompanyNotFoundException("Company not found"));
        return mapperUtil.convert(company, new CompanyDto());
    }

    @Override
    public List<CompanyDto> listAllCompanies() {
        List<Company> list = companyRepository.findAll();
        UserDto user = securityService.getLoggedInUser(); //loggedIn user
        List<CompanyDto> companies= new ArrayList<>();
        if (user.getRole().getDescription().equals("Root User")){
            companies= list.stream().filter(each -> !each.getTitle().equals(user.getCompany().getTitle())).map(each -> mapperUtil.convert(each, new CompanyDto())).toList();
        }else if(user.getRole().getDescription().equals("Admin")){
            companies= list.stream().filter(each -> each.getTitle().equals(user.getCompany().getTitle())).map(each -> mapperUtil.convert(each, new CompanyDto())).toList();
        }
        return companies;
    }

    @Override
    public CompanyDto saveCompany(CompanyDto dto) {
        dto.setCompanyStatus(CompanyStatus.PASSIVE);
        Company company = mapperUtil.convert(dto, new Company());
        Company entity = companyRepository.save(company);
        return mapperUtil.convert(entity, new CompanyDto());
    }

    @Override
    public CompanyDto activateCompany(Long id) {
        Company company = companyRepository.findById(id).orElseThrow(CompanyNotFoundException::new);
        company.setCompanyStatus(CompanyStatus.ACTIVE);
        Company entity = companyRepository.save(company);
        return mapperUtil.convert(entity, new CompanyDto());
    }

    @Override
    public CompanyDto deactivateCompany(Long id) {
        Company company = companyRepository.findById(id).orElseThrow(CompanyNotFoundException::new);
        company.setCompanyStatus(CompanyStatus.PASSIVE);
        Company entity = companyRepository.save(company);
        return mapperUtil.convert(entity, new CompanyDto());
    }

    @Override
    public CompanyDto updateCompany(CompanyDto dto) {
        Company company = companyRepository.findById(dto.getId()).orElseThrow(CompanyNotFoundException::new);
        Company updatedCompany = mapperUtil.convert(dto, new Company());
        updatedCompany.setCompanyStatus(company.getCompanyStatus());
        Company entity = companyRepository.save(updatedCompany);
        return mapperUtil.convert(entity, new CompanyDto());
    }

    @Override
    public boolean titleExist(CompanyDto dto) {
        Company company = companyRepository.findByTitle(dto.getTitle()).orElse(null);
        if (company == null) return false;
//        return !company.getTitle().equals(dto.getTitle());
        return !Objects.equals(dto.getId(), company.getId()); // we use Object to assert because of optional
    }

}
