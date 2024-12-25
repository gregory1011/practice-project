package com.app.repository;

import com.app.entity.Invoice;
import com.app.enums.InvoiceType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InvoiceRepository extends JpaRepository<Invoice, Long> {

    List<Invoice> findAllByCompanyIdAndInvoiceType(Long id, InvoiceType invoiceType);
    Invoice findFirstByCompanyIdAndInvoiceTypeOrderByInvoiceNoDesc(long companyId, InvoiceType invoiceType);
}
