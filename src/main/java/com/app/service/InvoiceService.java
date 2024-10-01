package com.app.service;

import com.app.dto.InvoiceDto;

public interface InvoiceService {

    InvoiceDto findInvoiceById(Long id);
}
