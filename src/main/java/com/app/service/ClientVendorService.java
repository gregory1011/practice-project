package com.app.service;

import com.app.dto.ClientVendorDto;
import com.app.enums.ClientVendorType;

import java.util.List;

public interface ClientVendorService {

    List<ClientVendorDto> listAllClientVendors();
    List<ClientVendorDto> listClientVendorsOrderByName();
    List<ClientVendorDto> listClientVendorsByClientVendorType(ClientVendorType clientVendorType);
}
