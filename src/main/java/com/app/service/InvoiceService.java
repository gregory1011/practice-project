package com.app.service;

import com.app.dto.InvoiceDto;
import java.util.List;

public interface InvoiceService {

    InvoiceDto findInvoiceById(Long id);
    List<InvoiceDto> findAllInvoiceByInvoiceType(String invoiceType);
//    List<InvoiceDto> findAllInvoice();
}
