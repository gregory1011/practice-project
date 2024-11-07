package com.app.service;

import com.app.dto.InvoiceDto;
import com.app.enums.InvoiceType;

import java.util.List;


public interface InvoiceService {

    InvoiceDto listInvoiceById(Long id);
    List<InvoiceDto> listAllByInvoiceType(InvoiceType invoiceType);
    List<InvoiceDto> listAllInvoiceByClientVendorId(Long id);
    InvoiceDto generateNewInvoiceDto(InvoiceType invoiceType);
    InvoiceDto saveInvoice(InvoiceDto invoiceDto, InvoiceType invoiceType);
    void updateInvoice(InvoiceDto dto);
    void deleteInvoice(Long id);
}
