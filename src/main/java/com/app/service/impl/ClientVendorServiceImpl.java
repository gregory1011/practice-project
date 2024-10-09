package com.app.service.impl;

import com.app.dto.ClientVendorDto;
import com.app.entity.ClientVendor;
import com.app.enums.ClientVendorType;
import com.app.repository.ClientVendorRepository;
import com.app.service.ClientVendorService;
import com.app.util.MapperUtil;
import lombok.AllArgsConstructor;
import org.apache.catalina.mapper.Mapper;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


@Service
@AllArgsConstructor
public class ClientVendorServiceImpl implements ClientVendorService {

    private final ClientVendorRepository clientVendorRepository;
    private final MapperUtil mapperUtil;

    @Override
    public List<ClientVendorDto> listAllClientVendors() {
        List<ClientVendor> list = clientVendorRepository.findAll();
        return list.stream().map(each -> mapperUtil.convert(each,new ClientVendorDto())).toList();
    }

    @Override
    public List<ClientVendorDto> listClientVendorsOrderByName() {
        return new ArrayList<>();
    }

    @Override
    public List<ClientVendorDto> listClientVendorsByClientVendorType(ClientVendorType clientVendorType) {
        List<ClientVendor> list = clientVendorRepository.findAllByClientVendorTypeOrderByClientVendorName(clientVendorType);
        return list.stream().map(each -> mapperUtil.convert(each,new ClientVendorDto())).toList();
    }

}
