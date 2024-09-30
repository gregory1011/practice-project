package com.app.entity;

import com.app.entity.common.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.math.BigDecimal;


@Getter
@Setter
@Entity
@Table(name = "invoice_products")
@NoArgsConstructor
@AllArgsConstructor
public class InvoiceProduct extends BaseEntity {

    private int quantity;
    private BigDecimal price;
    private int tax;
    private BigDecimal profitLoss;
    @ManyToOne()
    private Invoice invoice;
    @ManyToOne()
    private Product product;
}
