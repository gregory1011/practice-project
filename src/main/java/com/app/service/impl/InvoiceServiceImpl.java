package com.app.service.impl;

import com.app.dto.InvoiceDto;
import com.app.dto.InvoiceProductDto;
import com.app.entity.InvoiceProduct;
import com.app.enums.InvoiceType;
import com.app.repository.InvoiceProductRepository;
import com.app.service.InvoiceService;
import com.app.util.MapperUtil;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
public class InvoiceServiceImpl implements InvoiceService {

    private final InvoiceProductRepository productRepository;
    private final MapperUtil mapperUtil;

    public InvoiceServiceImpl(InvoiceProductRepository productRepository, MapperUtil mapperUtil) {
        this.productRepository = productRepository;
        this.mapperUtil = mapperUtil;
    }

    @Override
    public InvoiceDto findInvoiceById(Long id) {
        Optional<InvoiceProduct> item = productRepository.findById(id);
        if (item.isPresent()) return mapperUtil.convert(item, new InvoiceDto());
        else return null;
    }

    @Override
    public List<InvoiceDto> findAllInvoiceByInvoiceType(String invoiceType) {

        List<InvoiceProduct> list = productRepository.findAllByInvoice_InvoiceType(InvoiceType.valueOf(invoiceType));
        return list.stream().map(each->mapperUtil.convert(each, new InvoiceDto())).collect(Collectors.toList());
    }

}
