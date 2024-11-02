package com.app.repository;
import com.app.entity.InvoiceProduct;
import com.app.enums.ClientVendorType;
import com.app.enums.InvoiceType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface InvoiceProductRepository extends JpaRepository<InvoiceProduct, Long> {

    List<InvoiceProduct> findAllByInvoice_InvoiceTypeAndInvoice_Company_TitleOrderByInvoice_InvoiceNoDesc(InvoiceType invoiceType, String title);

    @Query( value = "select * from invoice_products P join invoices I on P.invoice_id = I.id where invoice_type='PURCHASE';", nativeQuery = true)
    List<InvoiceProduct> listAllInvoiceProductsByInvoiceTypePurchase();


}
