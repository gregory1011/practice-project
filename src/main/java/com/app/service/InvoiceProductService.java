package com.app.service;

import com.app.dto.InvoiceProductDto;

import java.util.List;

public interface InvoiceProductService {

    InvoiceProductDto findById(Long id);
    List<InvoiceProductDto> listAllByInvoiceIdAndCalculateTotalPrice(Long id);
    List<InvoiceProductDto> listInvoiceProductByInvoiceId(Long invoiceId);
    InvoiceProductDto add(InvoiceProductDto invoiceProductDto, Long invoiceId);
    List<InvoiceProductDto> listAllApprovedInvoiceProductsOfCompany();
    InvoiceProductDto save(Long invoiceId, InvoiceProductDto dto);
    InvoiceProductDto deleteInvoiceProduct(Long invoiceProductId);
    void updateRemainingQuantityUponPurchaseApproval(Long id);
    void updateQuantityInStockPurchase(Long id);
    void updateQuantityInStockSale(Long id);
    void calculateProfitOrLoss(Long id);
    void checkForLowQuantityAlert(Long invoiceId);
}
