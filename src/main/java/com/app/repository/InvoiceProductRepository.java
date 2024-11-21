package com.app.repository;

import com.app.entity.InvoiceProduct;
import com.app.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface InvoiceProductRepository extends JpaRepository<InvoiceProduct, Long> {

    List<InvoiceProduct> findAllByInvoiceId(Long id);

    @Query(value = "SELECT DISTINCT ip.product FROM invoice_products ip WHERE ip.invoice.id = ?1")
    List<Product> listProductsByInvoiceId(Long invoiceId);

    @Query(value = "SELECT SUM(ip.quantity) FROM invoice_products ip WHERE ip.invoice.id = ?1 AND ip.product.id=?2")
    Integer sumQuantityOfProducts(Long invoiceId, Long productId);

    @Query(value = "SELECT ip FROM invoice_products ip " +
            "WHERE ip.product.id = ?2 AND ip.remainingQuantity>0 " +
            "AND ip.invoice.invoiceStatus = 'APPROVED' AND ip.invoice.invoiceType = 'PURCHASE' " +
            "AND ip.invoice.company.id=?1 " +
            "order by ip.id asc ")
    List<InvoiceProduct> getApprovedPurchaseInvoiceProducts(Long companyId, Long productId);
}
