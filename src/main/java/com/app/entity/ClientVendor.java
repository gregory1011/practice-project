package com.app.entity;

import com.app.entity.common.BaseEntity;
import com.app.enums.ClientVendorType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;


@Entity
@Table(name = "clients_vendors")
public class ClientVendor extends BaseEntity {

    private String clientVendorName;
    private String phone;
    private String website;
    @Enumerated(EnumType.STRING)
    private ClientVendorType clientVendorType;
    @OneToOne(fetch = FetchType.LAZY)
    private Address address;
    @ManyToOne(fetch = FetchType.LAZY)
    private Company company;
}
