package com.app.entity;

import com.app.entity.common.BaseEntity;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Getter
@Setter
@Entity(name = "roles")
public class Role extends BaseEntity {

    @Column(nullable = false)
    private String description;
}
