package com.app.service.impl;

import com.app.dto.InvoiceProductDto;
import com.app.repository.InvoiceProductRepository;
import com.app.service.InvoiceProductService;
import org.springframework.stereotype.Service;

@Service
public class InvoiceProductServiceImpl implements InvoiceProductService {

    private final InvoiceProductRepository invoiceProductRepository;

    public InvoiceProductServiceImpl(InvoiceProductRepository invoiceProductRepository) {
        this.invoiceProductRepository = invoiceProductRepository;
    }


    @Override
    public InvoiceProductDto findInvoiceProductById(Long id) {
        return null;
    }
}
