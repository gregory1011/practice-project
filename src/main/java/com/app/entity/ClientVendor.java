package com.app.entity;

import com.app.entity.common.BaseEntity;
import com.app.enums.ClientVendorType;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.List;

@Getter
@Setter
@Entity(name = "clients_vendors")
@Where(clause = "is_deleted=false")
public class ClientVendor extends BaseEntity {

    @Column(nullable = false)
    private String clientVendorName;

    @Column(nullable = false)
    private String phone;

    @Column(nullable = false)
    private String website;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private ClientVendorType clientVendorType;

    @OneToOne(cascade = CascadeType.PERSIST)
    private Address address;

    @NotNull
    @ManyToOne()
    private Company company;

    @OneToMany(fetch = FetchType.EAGER)
    private List<Invoice> invoices;
}
