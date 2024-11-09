package com.app.entity;

import com.app.entity.common.BaseEntity;
import lombok.*;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import javax.validation.Constraint;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

@Getter
@Setter
@Entity(name = "categories")
@Where(clause = "is_deleted=false")
public class Category extends BaseEntity {


    @Column(nullable = false)
    private String description;

    @JoinColumn(nullable = false)
    @ManyToOne()
    private Company company;

    @OneToMany(mappedBy = "category")
    private List<Product> product;
}
