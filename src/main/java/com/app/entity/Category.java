package com.app.entity;

import com.app.entity.common.BaseEntity;
import lombok.*;

import javax.persistence.*;


@Getter
@Setter
@ToString
@Entity
@Table(name = "categories")
@NoArgsConstructor
@AllArgsConstructor
public class Category extends BaseEntity {

    private String description;
    @ManyToOne(fetch = FetchType.LAZY)
    private Company company;
}
