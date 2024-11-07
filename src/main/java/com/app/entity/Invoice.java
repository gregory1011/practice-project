package com.app.entity;

import com.app.dto.UserDto;
import com.app.entity.common.BaseEntity;
import com.app.enums.InvoiceStatus;
import com.app.enums.InvoiceType;
import javax.persistence.*;
import java.time.LocalDate;


@Entity
@Table(name = "invoices")
public class Invoice extends BaseEntity {

    private String invoiceNo;
    @Enumerated(EnumType.STRING)
    private InvoiceStatus invoiceStatus;
    @Enumerated(EnumType.STRING)
    private InvoiceType invoiceType;
    private LocalDate date;

    @ManyToOne(fetch = FetchType.LAZY)
    private ClientVendor clientVendor;

    @ManyToOne(fetch = FetchType.LAZY)
    private Company company;
}
