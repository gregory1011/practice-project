package com.app.util;

import com.app.dto.CompanyDto;
import com.app.entity.Company;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class CompanyMapper {

    private final ModelMapper modelMapper;

    public Company convertToEntity(CompanyDto companyDto) {
        return modelMapper.map(companyDto, Company.class);
    }
    public CompanyDto convertToDto(Company company) {
        return modelMapper.map(company, CompanyDto.class);
    }
}
