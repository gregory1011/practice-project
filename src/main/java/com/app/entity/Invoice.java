package com.app.entity;

import com.app.entity.common.BaseEntity;
import com.app.enums.InvoiceStatus;
import com.app.enums.InvoiceType;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;

@Getter
@Setter
@ToString
@Entity
@Table(name = "invoices")
@NoArgsConstructor
@AllArgsConstructor
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
