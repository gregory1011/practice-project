package com.app.service;

import com.app.dto.ClientVendorDto;
import com.app.enums.ClientVendorType;

import java.util.List;

public interface ClientVendorService {

    List<ClientVendorDto> listAllClientVendors();
    List<ClientVendorDto> listAllByClientVendorTypeAndCompanyId(ClientVendorType clientVendorType);
    void saveClientVendor(ClientVendorDto clientVendorDto);
    void deleteClientVendor(Long id);
    ClientVendorDto getClientVendorById(Long id);
    void updateClientVendor(Long id, ClientVendorDto clientVendor);
}
