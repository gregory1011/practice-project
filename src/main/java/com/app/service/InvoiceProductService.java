package com.app.service;

import com.app.dto.InvoiceProductDto;
import com.app.entity.InvoiceProduct;
import com.app.enums.InvoiceType;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

public interface InvoiceProductService {


    InvoiceProductDto findInvoiceProductById(Long id);
//    InvoiceProductDto getAllByInvoiceType(InvoiceType invoiceType);

    InvoiceProductDto findInvoiceProductByInvoiceId(Long id);

    List<InvoiceProductDto> listAllInvoiceProductsByInvoiceType(InvoiceType invoiceType);

    List<InvoiceProductDto> listAllInvoiceProductsByInvoiceTypePurchase();

//    @Query("select ip from InvoiceProduct ip join Invoice i on ip.invoice.id = i.id where i.invoiceType='Purchase'")
//    List<InvoiceProductDto> findAllProductsByInvoiceTypePurchase();

}
