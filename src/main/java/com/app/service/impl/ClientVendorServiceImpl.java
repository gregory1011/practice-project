package com.app.service.impl;

import com.app.dto.ClientVendorDto;
import com.app.entity.ClientVendor;
import com.app.enums.ClientVendorType;
import com.app.exceptions.ClientVendorNotFoundException;
import com.app.repository.ClientVendorRepository;
import com.app.service.ClientVendorService;
import com.app.service.CompanyService;
import com.app.util.MapperUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
//import static org.junit.jupiter.api.Assertions.*;


@Service
@RequiredArgsConstructor
public class ClientVendorServiceImpl implements ClientVendorService {

    private final ClientVendorRepository clientVendorRepository;
    private final MapperUtil mapperUtil;
    private final CompanyService companyService;


    @Override
    @Transactional(readOnly = true)
    public ClientVendorDto findById(Long id) {
        ClientVendor clientVendor = clientVendorRepository.findById(id).orElseThrow(ClientVendorNotFoundException::new);
        return mapperUtil.convert(clientVendor,new ClientVendorDto());
    }

    @Override
    public List<ClientVendorDto> listAllClientVendors() {
        Long id = companyService.getCompanyByLoggedInUser().getId();
        return clientVendorRepository.findByCompanyId(id).stream()
                .map(each ->{
                    ClientVendorDto dto= mapperUtil.convert(each, new ClientVendorDto());
                    dto.setHasInvoice(!each.getInvoices().isEmpty());
                    return dto;
                })
                .sorted(Comparator.comparing(ClientVendorDto::getClientVendorType).reversed()
                        .thenComparing(ClientVendorDto::getClientVendorName))
                .toList();
    }

    @Override
    public List<ClientVendorDto> listAllByClientVendorType(ClientVendorType clientVendorType) {
        Long id = companyService.getCompanyByLoggedInUser().getId();
        return clientVendorRepository.findAll().stream()
                .filter(m->m.getClientVendorType().equals(clientVendorType))
                .filter(m->m.getCompany().getId().equals(id))
                .map(each->mapperUtil.convert(each, new ClientVendorDto()))
                .toList();
    }

    @Override
    public ClientVendorDto saveClientVendor(ClientVendorDto dto) {
        dto.setCompany(companyService.getCompanyByLoggedInUser());
        ClientVendor saved = clientVendorRepository.save(mapperUtil.convert(dto, new ClientVendor()));
        return mapperUtil.convert(saved, new ClientVendorDto());
    }

    @Override
    public ClientVendorDto updateClientVendor(ClientVendorDto dto) {
        ClientVendor clientVendor = clientVendorRepository.findById(dto.getId()).orElseThrow(ClientVendorNotFoundException::new);

        dto.getAddress().setId(clientVendor.getAddress().getId());
        dto.setCompany(companyService.getCompanyByLoggedInUser());

        ClientVendor saved = clientVendorRepository.save(mapperUtil.convert(dto, new ClientVendor()));
        return mapperUtil.convert(saved, new ClientVendorDto());
    }

    @Override
    public void deleteClientVendor(Long id) {
        ClientVendor clientVendor = clientVendorRepository.findById(id).orElseThrow(ClientVendorNotFoundException::new);
        clientVendor.setIsDeleted(true);
        clientVendor.getAddress().setIsDeleted(true);
        clientVendor.setClientVendorName(clientVendor.getClientVendorName()+ "-"+id);
        clientVendorRepository.save(clientVendor);
    }

    @Override
//    @Transactional(readOnly = true)
    public boolean isClientVendorNameExists(ClientVendorDto dto) {
        Long companyId = companyService.getCompanyByLoggedInUser().getId();
        ClientVendor result = clientVendorRepository.findByCompanyIdAndClientVendorName(companyId, dto.getClientVendorName()).orElse(null);
        if (result == null) return false;
        return !Objects.equals(dto.getId(), result.getId());
    }

}
