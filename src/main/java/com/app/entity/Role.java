package com.app.entity;

import com.app.entity.common.BaseEntity;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Table;

@Getter
@Setter
@Entity
@Table(name = "roles")
public class Role extends BaseEntity {

    private String description;
}
