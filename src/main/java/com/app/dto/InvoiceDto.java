package com.app.dto;


import com.app.enums.InvoiceStatus;
import com.app.enums.InvoiceType;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDate;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InvoiceDto {

    private Long id;

    private String invoiceNo;

    private InvoiceStatus invoiceStatus;

    private InvoiceType invoiceType;

    @DateTimeFormat(pattern = "MMMM-dd-yyyy")
    private LocalDate date;

    private CompanyDto company;

    @NotNull(message = "Client/Vendor is a required field.")
    private ClientVendorDto clientVendor;

    private BigDecimal price;

    private BigDecimal tax;

    private BigDecimal total;

}
