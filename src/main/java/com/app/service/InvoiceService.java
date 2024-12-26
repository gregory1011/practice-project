package com.app.service;

import com.app.dto.InvoiceDto;
import com.app.enums.InvoiceType;

import java.math.BigDecimal;
import java.util.List;


public interface InvoiceService {

    InvoiceDto findById(Long id);
    List<InvoiceDto> listInvoices(InvoiceType invoiceType);
//    List<InvoiceDto> listAllInvoiceByClientVendorId(Long id);
    InvoiceDto generateNewInvoiceDto(InvoiceType invoiceType);
    InvoiceDto save(InvoiceDto invoiceDto, InvoiceType invoiceType);
    InvoiceDto approveInvoice(Long id);
    InvoiceDto updateInvoice(InvoiceDto dto);
    void deleteInvoice(Long id);
    List<InvoiceDto> listLast3ApprovedInvoices();
    BigDecimal sumTotal(InvoiceType invoiceType);
    BigDecimal sumProfitLoss();
    InvoiceDto printInvoiceId(Long id);
}

