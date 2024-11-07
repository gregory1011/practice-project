package com.app.service;

import com.app.dto.InvoiceProductDto;

import java.util.List;

public interface InvoiceProductService {

    InvoiceProductDto findInvoiceProductById(Long id);
    List<InvoiceProductDto> listAllByInvoiceIdAndCalculateTotalPrice(Long id);
    void saveInvoiceProduct(Long invoiceId, InvoiceProductDto dto);
    void deleteInvoiceProduct(Long invoiceProductId);
    List<InvoiceProductDto> findInvoiceProductByInvoiceId(Long invoiceId);
}
