package com.app.service;

import com.app.dto.InvoiceDto;
import com.app.entity.Invoice;

public interface InvoiceService {

    InvoiceDto findInvoiceById(Integer id);
}
