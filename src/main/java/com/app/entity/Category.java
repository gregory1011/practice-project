package com.app.entity;

import com.app.entity.common.BaseEntity;
import lombok.*;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;


@Getter
@Setter
@ToString
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Category extends BaseEntity {

    private String description;
    @ManyToOne()
    @JoinColumn(name = "categories")
    private Company company;
}
