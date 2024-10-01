package com.app.repository;

import com.app.entity.InvoiceProduct;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InvoiceProductRepository extends JpaRepository<InvoiceProduct, Long> {


}
