package com.app.repository;

import com.app.entity.ClientVendor;
import com.app.enums.ClientVendorType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface ClientVendorRepository extends JpaRepository<ClientVendor, Long> {

    List<ClientVendor> findAllByClientVendorTypeOrderByClientVendorName(ClientVendorType clientVendorType);
}
