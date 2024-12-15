package com.app.repository;

import com.app.entity.ClientVendor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface ClientVendorRepository extends JpaRepository<ClientVendor, Long> {

    List<ClientVendor> findByCompanyId(Long companyId);
    Optional<ClientVendor> findByCompanyIdAndClientVendorName(Long companyId, String clientVendorName);
}
