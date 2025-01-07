package com.app.service;

import com.app.dto.*;
import com.app.enums.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Month;


public class TestDocInitializer {

    public static AddressDto getAddress(){
        return AddressDto.builder()
                .id(1L)
                .addressLine1("188 Glendale Rd")
                .addressLine2("Suite 3")
                .city("Pomegranate")
                .state("California")
                .zipCode("90302")
                .country("US")
                .build();
    }
    public static CategoryDto getCategory(){
        return CategoryDto.builder()
                .id(1L)
                .description("Test Category")
                .company(getCompany(CompanyStatus.ACTIVE))
                .build();
    }
    public static ClientVendorDto getClientVendorDto(ClientVendorType type){
        return ClientVendorDto.builder()
                .id(1L)
                .clientVendorType(type)
                .clientVendorName("test")
                .address(getAddress())
                .company(getCompany(CompanyStatus.ACTIVE))
                .website("www.test.net")
                .phone("123-456-1233")
                .build();
    }
    public static CompanyDto getCompany(CompanyStatus status) {
        return CompanyDto.builder()
                .id(1L)
                .title("Test_Company")
                .website("www.test.net")
                .phone("213-440-9044")
                .companyStatus(status)
                .address(getAddress())
                .build();
    }
    public static InvoiceDto getInvoiceDto(InvoiceStatus status, InvoiceType type){
        return InvoiceDto.builder()
                .id(1L)
                .invoiceNo("T-100")
                .clientVendor(getClientVendorDto(ClientVendorType.CLIENT))
                .invoiceStatus(status)
                .invoiceType(type)
                .date(LocalDate.of(2024,1,1))
                .price(BigDecimal.valueOf(100))
                .tax(BigDecimal.TEN)
                .total(BigDecimal.TEN.multiply(BigDecimal.valueOf(100)))
                .build();
    }
    public static InvoiceProductDto getInvoiceProductDto(){
        return InvoiceProductDto.builder()
                .id(1L)
                .invoice(new InvoiceDto())
                .product(getProductDto())
                .price(BigDecimal.TEN)
                .tax(10)
                .quantity(5)
                .build();
    }
    public static PaymentDto getPaymentDto(){
        return PaymentDto.builder()
                .id(1L)
                .description("Test Payment")
                .amount(BigDecimal.TEN)
                .month(Month.JANUARY)
                .year(2024)
                .paymentDate(LocalDate.now())
                .isPaid(true)
                .companyStripeId("1K12Oxfr#401")
                .company(new CompanyDto())
                .build();
    }
    public static ProductDto getProductDto(){
        return ProductDto.builder()
                .id(1L)
                .name("test_product")
                .category(getCategory())
                .productUnit(ProductUnit.KG)
                .lowLimitAlert(5)
                .quantityInStock(10)
                .build();
    }
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


}
