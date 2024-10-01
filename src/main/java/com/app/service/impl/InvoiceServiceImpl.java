package com.app.service.impl;

import com.app.dto.InvoiceDto;
import com.app.entity.Invoice;
import com.app.service.InvoiceService;
import org.springframework.stereotype.Service;

@Service
public class InvoiceServiceImpl implements InvoiceService {

    private final InvoiceService invoiceService;

    public InvoiceServiceImpl(InvoiceService invoiceService) {
        this.invoiceService = invoiceService;
    }

    @Override
    public InvoiceDto findInvoiceById(Long id) {
        return null;
    }


}
