package com.app.entity;

import com.app.entity.common.BaseEntity;
import javax.persistence.Entity;
import javax.persistence.Table;


@Entity
@Table(name = "roles")
public class Role extends BaseEntity {

    private String description;
}
