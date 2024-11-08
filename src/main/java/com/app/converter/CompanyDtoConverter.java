package com.app.converter;


import com.app.dto.CompanyDto;
import com.app.entity.Company;
import com.app.service.CompanyService;
import lombok.AllArgsConstructor;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class CompanyDtoConverter implements Converter<String, CompanyDto> {

    private final CompanyService companyService;

    @Override
    public CompanyDto convert(String source) {
        if (source.isEmpty()) return null;
        return companyService.listCompanyById(Long.parseLong(source));
    }
}
