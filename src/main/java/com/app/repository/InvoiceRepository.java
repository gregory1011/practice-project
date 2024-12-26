package com.app.repository;

import com.app.entity.Invoice;
import com.app.enums.InvoiceType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InvoiceRepository extends JpaRepository<Invoice, Long> {

    List<Invoice> findAllByCompanyIdAndInvoiceType(Long id, InvoiceType invoiceType);
    Invoice findFirstByCompanyIdAndInvoiceTypeOrderByInvoiceNoDesc(long companyId, InvoiceType invoiceType);

    @Query(value = "select * from invoices i where i.company_id= ?1 and i.invoice_status= 'APPROVED' order by i.insert_date_time desc limit 3", nativeQuery = true)
    List<Invoice> findByCompanyIdOrderByInsertDateTimeDescLimit3(Long companyId);

    @Query(value = "select * from invoices i where i.company_id= ?1 and i.invoice_type= 'SALES' and i.invoice_status= 'APPROVED'", nativeQuery = true)
    List<Invoice> findByCompanyIdAndInvoiceTypeSalesAndInvoiceStatusApproved(Long companyId);
}
