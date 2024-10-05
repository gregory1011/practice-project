package com.app.service.impl;

import com.app.dto.InvoiceDto;
import com.app.dto.InvoiceProductDto;
import com.app.entity.InvoiceProduct;
import com.app.enums.InvoiceType;
import com.app.repository.InvoiceProductRepository;
import com.app.repository.UserRepository;
import com.app.service.InvoiceProductService;
import com.app.util.MapperUtil;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class InvoiceProductServiceImpl implements InvoiceProductService {

    private final InvoiceProductRepository productRepository;
    private final MapperUtil mapperUtil;
    private final UserRepository userRepository;

    public InvoiceProductServiceImpl(InvoiceProductRepository invoiceProductRepository, MapperUtil mapperUtil, UserRepository userRepository) {
        this.productRepository = invoiceProductRepository;
        this.mapperUtil = mapperUtil;
        this.userRepository = userRepository;
    }

    @Override
    public InvoiceProductDto findInvoiceProductById(Long id) {

        Optional<InvoiceProduct> invoiceProduct = productRepository.findById(id);
        if (invoiceProduct.isPresent()) return mapperUtil.convert(invoiceProduct, new InvoiceProductDto());
        else return null;
    }

    @Override
    public InvoiceProductDto findInvoiceProductByInvoiceId(Long id) {
        InvoiceProduct item = productRepository.findById(id).orElse(null);
        return mapperUtil.convert(item, new InvoiceProductDto());
    }

    @Override
    public List<InvoiceProductDto> listAllInvoiceProductsByInvoiceType(InvoiceType invoiceType) {
        List<InvoiceProduct> list = productRepository.getAllByInvoice_InvoiceType(invoiceType);

        return list.stream().map(each-> mapperUtil.convert(each, new InvoiceProductDto())).collect(Collectors.toList());
    }

    @Override
    public List<InvoiceProductDto> listAllInvoiceProductsByInvoiceTypePurchase() {

        List<InvoiceProduct> list = productRepository.listAllInvoiceProductsByInvoiceTypePurchase();
        return list.stream().map(each-> mapperUtil.convert(each, new InvoiceProductDto())).collect(Collectors.toList());
    }

    @Override
    public List<InvoiceProductDto> listAllInvoiceByInvoiceTypeOrderByInvoiceNoDes(InvoiceType invoiceType) {


        return List.of();
    }


}
