package com.app.repository;
import com.app.entity.InvoiceProduct;
import com.app.entity.User;
import com.app.enums.InvoiceType;
import org.springframework.data.repository.query.Param;
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

    @Query( value = "select * from invoice_products P join invoices I on P.invoice_id = I.id where invoice_type='PURCHASE';", nativeQuery = true)
    List<InvoiceProduct> listAllInvoiceProductsByInvoiceTypePurchase();

//    -------- 3rd user story

    @Query(value = "select * from invoice_products P join invoices I on P.invoice_id = I.id where invoice_type= ?1 order by invoice_no desc", nativeQuery = true)
    List<InvoiceProduct> findAllByInvoice_InvoiceTypeAndOrderByInvoiceNoDesc(@Param("invoiceType")InvoiceType invoiceType);

    @Query(value = "select * from invoice_products P join invoices I on P.invoice_id = I.id join clients_vendors C on I.client_vendor_id = C.id join  companies CO on C.company_id = CO.id where title= ?1 order by invoice_no desc", nativeQuery = true)
    List<InvoiceProduct> findInvoiceProductByInvoiceNo(@Param("title")String title);


}
