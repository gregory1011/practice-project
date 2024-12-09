package com.app.service;

import com.app.dto.InvoiceProductDto;

import java.util.List;

public interface InvoiceProductService {

    InvoiceProductDto listInvoiceProductById(Long id);
    List<InvoiceProductDto> listAllByInvoiceIdAndCalculateTotalPrice(Long id);
    List<InvoiceProductDto> listInvoiceProductByInvoiceId(Long invoiceId);
    List<InvoiceProductDto> listAllApprovedInvoiceProductsOfCompany();
    void saveInvoiceProduct(Long invoiceId, InvoiceProductDto dto);
    void deleteInvoiceProduct(Long invoiceProductId);
    void updateRemainingQuantityUponPurchaseApproval(Long id);
    void updateQuantityInStockPurchase(Long id);
    void updateQuantityInStockSale(Long id);
    void calculateProfitOrLoss(Long id);
}
