package com.app.repository;
import com.app.entity.InvoiceProduct;
import com.app.enums.InvoiceType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface InvoiceProductRepository extends JpaRepository<InvoiceProduct, Long> {

    List<InvoiceProduct> findAllByInvoice_InvoiceType(InvoiceType invoiceType);
    List<InvoiceProduct> findInvoiceProductByInvoice_InvoiceType(InvoiceType invoiceType);
    List<InvoiceProduct> findInvoiceProductByInvoice_Id(Long id);
    List<InvoiceProduct> getAllByInvoice_InvoiceType(InvoiceType invoiceType);


//    @Query(value = "select ip from invoice_products ip join invoices i on ip.invoice_id = i.id where i.invoice_type='PURCHASE'", nativeQuery = true)
//    List<InvoiceProduct> findAllProductsByInvoiceTypePurchase();

    @Query( value = "select * from invoice_products P join invoices I on P.invoice_id = I.id where invoice_type='PURCHASE';", nativeQuery = true)
    List<InvoiceProduct> listAllInvoiceProductsByInvoiceTypePurchase();

//    -------- 3rd user story

    @Query(value = "select * from invoice_products P join invoices I on P.invoice_id = I.id where", nativeQuery = true)
    List<InvoiceProduct> findAllByInvoice_InvoiceTypeAndOrderByInvoiceDesc(InvoiceType invoiceType);

}
