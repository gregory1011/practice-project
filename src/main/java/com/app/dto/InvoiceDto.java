package com.app.dto;


import com.app.enums.InvoiceStatus;
import com.app.enums.InvoiceType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.time.LocalDate;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class InvoiceDto {

    private Long id;
    private String invoiceNo;
    private InvoiceStatus invoiceStatus;
    private InvoiceType invoiceType;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate date;
    private CompanyDto company;
    private ClientVendorDto clientVendor;
    BigDecimal price;
    BigDecimal tax;
    BigDecimal total;
}
