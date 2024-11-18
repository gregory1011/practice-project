package com.app.service;

import com.app.dto.AddressDto;
import com.app.dto.CompanyDto;
import com.app.dto.RoleDto;
import com.app.dto.UserDto;
import com.app.enums.CompanyStatus;

public class TestDocInitializer {

    public static UserDto getUser(String role){
        return UserDto.builder()
                .id(1L)
                .firstname("Jora")
                .lastname("Mora")
                .password("123-456-1233")
                .username("jora@cydeo.com")
                .password("Abc1")
                .confirmPassword("Abc1")
                .role(new RoleDto(1L, role))
                .isOnlyAdmin(false)
                .company(new CompanyDto())
                .build();
    }

    public static CompanyDto getCompany(CompanyStatus status) {
        return CompanyDto.builder()
                .id(1L)
                .title("Test_Company")
                .website("www.test.net")
                .phone("213-440-9044")
                .companyStatus(status)
                .address(new AddressDto())
                .build();
    }


}