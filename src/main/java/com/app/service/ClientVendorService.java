package com.app.service;

import com.app.dto.ClientVendorDto;
import com.app.enums.ClientVendorType;

import java.util.List;

public interface ClientVendorService {

    ClientVendorDto findById(Long id);
    List<ClientVendorDto> listAllClientVendors();
    List<ClientVendorDto> listAllByClientVendorType(ClientVendorType clientVendorType);
    ClientVendorDto saveClientVendor(ClientVendorDto clientVendorDto);
    ClientVendorDto updateClientVendor(ClientVendorDto clientVendor);
    void deleteClientVendor(Long id);
    boolean isClientVendorNameExists(String clientVendorName);
}
