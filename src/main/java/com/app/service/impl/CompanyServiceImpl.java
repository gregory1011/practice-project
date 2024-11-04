package com.app.service.impl;


import com.app.dto.CompanyDto;
import com.app.dto.UserDto;
import com.app.entity.Address;
import com.app.entity.Company;
import com.app.entity.User;
import com.app.enums.CompanyStatus;
import com.app.repository.CompanyRepository;
import com.app.repository.UserRepository;
import com.app.service.CompanyService;
import com.app.service.SecurityService;
import com.app.util.MapperUtil;
import lombok.AllArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class CompanyServiceImpl implements CompanyService {

    private final CompanyRepository companyRepository;
    private final MapperUtil mapperUtil;
    private final UserRepository userRepository;
    private final SecurityService securityService;


    @Override
    public CompanyDto getCompanyById(Long id) {
        Company company = companyRepository.findById(id).orElseThrow(() -> new RuntimeException("Company not found"));
        return mapperUtil.convert(company, new CompanyDto());
    }

    @Override
    public Long getCompanyIdByLoggedInUser(String companyId) {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username;
        if (principal instanceof UserDetails) username = ((UserDetails)principal).getUsername();
        else username = principal.toString();
        User loggedUser = userRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("User not found"));
        return loggedUser.getCompany().getId();
    }

    @Override
    public List<CompanyDto> listAllCompanies() {
        List<Company> list = companyRepository.findAll();
        UserDto user = securityService.getLoggedInUser();
        List<CompanyDto> companies= new ArrayList<>();
        if (user.getRole().getDescription().equals("Root User")){
           companies= list.stream().filter(each -> !each.getTitle().equals(user.getCompany().getTitle())).map(each -> mapperUtil.convert(each, new CompanyDto())).toList();
        }else if(user.getRole().getDescription().equals("Admin")){
            companies= list.stream().filter(each -> each.getTitle().equals(user.getCompany().getTitle())).map(each -> mapperUtil.convert(each, new CompanyDto())).toList();
        }
        return companies;
    }

    @Override
    public void saveCompany(CompanyDto dto) {
//        List<String> titles = companyRepository.findAll().stream().map(Company::getTitle).toList();
//        if(titles.contains(dto.getTitle())){
//            throw new RuntimeException("Title already exists");
//        }
        dto.setCompanyStatus(CompanyStatus.PASSIVE);
        Company company = mapperUtil.convert(dto, new Company());
        companyRepository.save(company);
    }

    @Override
    public void activateCompany(Long id) {
        Company company = companyRepository.findById(id).orElseThrow(() -> new RuntimeException("Company not found"));
        company.setCompanyStatus(CompanyStatus.ACTIVE);
        companyRepository.save(company);
    }

    @Override
    public void deactivateCompany(Long id) {
        Company company = companyRepository.findById(id).orElseThrow(() -> new RuntimeException("Company not found"));
        company.setCompanyStatus(CompanyStatus.PASSIVE);
        companyRepository.save(company);
    }

    @Override
    public void updateCompany(Long id, CompanyDto companyDto) {
        Company company = companyRepository.findById(id).orElseThrow(() -> new RuntimeException("Company not found"));
        company.setPhone(companyDto.getPhone());
        company.setTitle(companyDto.getTitle());
        company.setWebsite(companyDto.getWebsite());
        company.setAddress(mapperUtil.convert(companyDto.getAddress(), new Address()));
        companyRepository.save(company);
    }


}
