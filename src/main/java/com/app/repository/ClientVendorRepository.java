package com.app.repository;

import com.app.entity.ClientVendor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface ClientVendorRepository extends JpaRepository<ClientVendor, Long> {

}
