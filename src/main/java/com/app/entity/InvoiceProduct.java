package com.app.entity;

import com.app.entity.common.BaseEntity;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.List;


@Entity
@Table(name = "invoice_products")
public class InvoiceProduct extends BaseEntity {

    private int quantity;
    private BigDecimal price;
    private int tax;
    private BigDecimal profitLoss;
    private int remainingQuantity;

    @ManyToOne()
    private Product product;

    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private Invoice invoice;
}
