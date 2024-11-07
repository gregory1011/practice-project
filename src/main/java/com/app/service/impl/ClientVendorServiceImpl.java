package com.app.service.impl;

import com.app.dto.ClientVendorDto;
import com.app.dto.CompanyDto;
import com.app.entity.ClientVendor;
import com.app.enums.ClientVendorType;
import com.app.repository.ClientVendorRepository;
import com.app.service.ClientVendorService;
import com.app.service.SecurityService;
import com.app.util.MapperUtil;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;


@Service
@AllArgsConstructor
public class ClientVendorServiceImpl implements ClientVendorService {

    private final ClientVendorRepository clientVendorRepository;
    private final MapperUtil mapperUtil;
    private final SecurityService securityService;

    @Override
    public List<ClientVendorDto> listAllClientVendors() {
        Long id = securityService.getLoggedInUser().getCompany().getId();
        return clientVendorRepository.findAll().stream()
                .filter(m->m.getCompany().getId().equals(id) && !m.getIsDeleted())
                .map(each -> mapperUtil.convert(each,new ClientVendorDto()))
                .sorted(Comparator.comparing(ClientVendorDto::getClientVendorType).reversed())
                .toList();
    }

    @Override
    public List<ClientVendorDto> listClientVendorsByClientVendorType(ClientVendorType clientVendorType) {
        List<ClientVendor> list = clientVendorRepository.findAllByClientVendorTypeOrderByClientVendorName(clientVendorType);
        return list.stream().map(each -> mapperUtil.convert(each,new ClientVendorDto())).toList();
    }

    @Override
    public void saveClientVendor(ClientVendorDto clientVendorDto) {
        CompanyDto company = securityService.getLoggedInUser().getCompany();
        clientVendorDto.setCompany(company);
        ClientVendor obj = mapperUtil.convert(clientVendorDto, new ClientVendor());
        clientVendorRepository.save(obj);
    }

    @Override
    public void deleteClientVendor(Long id) {
        Optional<ClientVendor> clientVendor= clientVendorRepository.findById(id);
        clientVendor.ifPresent(client -> client.setIsDeleted(true));
        clientVendorRepository.save(clientVendor.get());
    }

    @Override
    public ClientVendorDto getClientVendorById(Long id) {
        ClientVendor clientVendor = clientVendorRepository.findById(id).orElseThrow(RuntimeException::new);
        return mapperUtil.convert(clientVendor,new ClientVendorDto());
    }

    @Override
    public void updateClientVendor(Long id, ClientVendorDto dto) {
        ClientVendor clientVendor = clientVendorRepository.findById(id).orElseThrow(RuntimeException::new);
        ClientVendor convert = mapperUtil.convert(dto, new ClientVendor());
        clientVendor.setAddress(convert.getAddress());
        clientVendor.setClientVendorType(dto.getClientVendorType());
        clientVendor.setClientVendorName(dto.getClientVendorName());
        clientVendor.setPhone(dto.getPhone());
        clientVendor.setWebsite(dto.getWebsite());
        clientVendorRepository.save(clientVendor);
    }

}
