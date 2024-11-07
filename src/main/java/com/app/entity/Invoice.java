package com.app.entity;

import com.app.dto.UserDto;
import com.app.entity.common.BaseEntity;
import com.app.enums.InvoiceStatus;
import com.app.enums.InvoiceType;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.time.LocalDate;

@Getter
@Setter
@Entity(name = "invoices")
@Where(clause = "is_deleted=false")
public class Invoice extends BaseEntity {

    private String invoiceNo;

    @Enumerated(EnumType.STRING)
    private InvoiceStatus invoiceStatus;

    @Enumerated(EnumType.STRING)
    private InvoiceType invoiceType;

    private LocalDate date;

    @ManyToOne()
    private ClientVendor clientVendor;

    @ManyToOne()
    private Company company;

}
