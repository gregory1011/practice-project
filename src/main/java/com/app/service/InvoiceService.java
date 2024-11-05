package com.app.service;

import com.app.dto.InvoiceDto;
import com.app.enums.InvoiceType;

import java.util.List;

public interface InvoiceService {

    InvoiceDto findInvoiceById(Long id);
    List<InvoiceDto> listAllInvoiceByInvoiceTypeOrderByInvoiceNo(InvoiceType invoiceType);
    String getLastInvoiceId(InvoiceType invoiceType);
    void saveSaleInvoice(InvoiceDto invoiceDto);
    void savePurchaseInvoice(InvoiceDto invoiceDto);
    List<InvoiceDto> listAllInvoiceByClientVendorId(Long id);
}
