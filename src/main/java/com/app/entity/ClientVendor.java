package com.app.entity;

import com.app.entity.common.BaseEntity;
import com.app.enums.ClientVendorType;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity(name = "clients_vendors")
public class ClientVendor extends BaseEntity {

    private String clientVendorName;

    private String phone;

    private String website;

    @Enumerated(EnumType.STRING)
    private ClientVendorType clientVendorType;

    @OneToOne(cascade = CascadeType.PERSIST)
    private Address address;

    @ManyToOne()
    private Company company;
}
