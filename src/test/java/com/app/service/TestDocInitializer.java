package com.app.service;

import com.app.dto.*;
import com.app.enums.*;


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
                .company(getCompany(CompanyStatus.ACTIVE))
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

    public static CategoryDto getCategory(){
        return CategoryDto.builder()
                .id(1L)
                .company(getCompany(CompanyStatus.ACTIVE))
                .description("Test Category")
                .build();
    }

    public static ClientVendorDto getClientVendorDto(ClientVendorType clientVendorType){
        ClientVendorDto clientVendorDto = new ClientVendorDto();
        clientVendorDto.setId(1L);
        clientVendorDto.setCompany(getCompany(CompanyStatus.ACTIVE));
        clientVendorDto.setPhone("123-456-1233");
        clientVendorDto.setWebsite("www.test.net");
        clientVendorDto.setClientVendorType(clientVendorType);
        clientVendorDto.setAddress(getAddress());
        clientVendorDto.setCompany(getCompany(CompanyStatus.ACTIVE));
        return clientVendorDto;
    }

    public static AddressDto getAddress(){
        AddressDto addressDto = new AddressDto();
        addressDto.setId(1L);
        addressDto.setCountry("US");
        addressDto.setAddressLine1("120 West Ave");
        addressDto.setCity("New York");
        addressDto.setState("NY");
        addressDto.setAddressLine2("Suite 10");
        addressDto.setZipCode("90904");
        return addressDto;
    }

    public static InvoiceDto getInvoiceDto(InvoiceStatus status, InvoiceType invoiceType){
        InvoiceDto invoiceDto = new InvoiceDto();
        invoiceDto.setId(1L);
        invoiceDto.setCompany(getCompany(CompanyStatus.ACTIVE));
        invoiceDto.setInvoiceNo("P-123");
        invoiceDto.setInvoiceStatus(status);
        invoiceDto.setInvoiceType(invoiceType);
        return invoiceDto;
    }

}
