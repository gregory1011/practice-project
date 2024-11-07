package com.app.entity;

import com.app.entity.common.BaseEntity;
import com.app.enums.ProductUnit;
import javax.persistence.*;


@Entity
@Table(name = "products")
public class Product extends BaseEntity {

    private String name;
    private int quantityInStock;
    private int lowLimitAlert;
    @Enumerated(EnumType.STRING)
    private ProductUnit productUnit;

    @ManyToOne(fetch = FetchType.LAZY)
    private Category category;
}
