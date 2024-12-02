package com.app.entity;

import com.app.entity.common.BaseEntity;
import com.app.enums.CompanyStatus;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Where;
import javax.persistence.*;



@Getter
@Setter
@Entity(name = "companies")
@Where(clause = "is_deleted=false")
public class Company extends BaseEntity {

    @Column(unique = true, nullable = false)
    private String title;

    @Column(nullable = false)
    private String phone;

    @Column(nullable = false)
    private String website;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private CompanyStatus companyStatus;

    @JoinColumn(nullable = false)
    @OneToOne(cascade ={CascadeType.PERSIST, CascadeType.MERGE})
    private Address address;
}
