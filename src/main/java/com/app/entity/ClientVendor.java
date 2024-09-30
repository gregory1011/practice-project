package com.app.entity;

import com.app.entity.common.BaseEntity;
import com.app.enums.ClientVendorType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;


@Getter
@Setter
@Entity
@Table(name = "clients_vendors")
@NoArgsConstructor
@AllArgsConstructor
public class ClientVendor extends BaseEntity {

    private String clientVendorName;
    private String phone;
    private String website;
    @Enumerated(EnumType.STRING)
    private ClientVendorType clientVendorType;
    @OneToOne()
    private Address address;
    @ManyToOne()
    private Company company;
}
