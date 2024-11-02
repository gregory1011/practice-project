package com.app.entity;

import com.app.entity.common.BaseEntity;
import com.app.enums.CompanyStatus;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;


@Getter
@Setter
@Entity
@Table(name = "companies")
public class Company extends BaseEntity {

    @Column(unique = true, nullable = false)
    private String title;
    private String phone;
    private String website;
    @Enumerated(EnumType.STRING)
    private CompanyStatus companyStatus;
    @OneToOne()
    private Address address;
}
