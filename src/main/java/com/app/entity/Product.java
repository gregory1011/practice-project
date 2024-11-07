package com.app.entity;

import com.app.entity.common.BaseEntity;
import com.app.enums.ProductUnit;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "products")
@Where(clause = "is_deleted=false")
public class Product extends BaseEntity {

    private String name;

    private int quantityInStock;

    private int lowLimitAlert;

    @Enumerated(EnumType.STRING)
    private ProductUnit productUnit;

    @ManyToOne()
    private Category category;

    @OneToMany(mappedBy = "product")
    private List<InvoiceProduct> invoiceProducts;

}
