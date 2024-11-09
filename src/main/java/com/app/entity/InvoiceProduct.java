package com.app.entity;

import com.app.entity.common.BaseEntity;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@Entity(name = "invoice_products")
@Where(clause = "is_deleted=false")
public class InvoiceProduct extends BaseEntity {

    @Column(nullable = false)
    private int quantity;

    @Column(nullable = false)
    private BigDecimal price;

    @Column(nullable = false)
    private int tax;

    private BigDecimal profitLoss;

    private int remainingQuantity;

    @JoinColumn(nullable = false)
    @ManyToOne()
    private Product product;

    @JoinColumn(nullable = false)
    @ManyToOne()
    private Invoice invoice;

}
