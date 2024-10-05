package com.app.entity;

import com.app.entity.common.BaseEntity;
import lombok.*;

import javax.persistence.*;



@Entity
@Table(name = "categories")
public class Category extends BaseEntity {

    private String description;
    @ManyToOne(fetch = FetchType.LAZY)
    private Company company;
}
