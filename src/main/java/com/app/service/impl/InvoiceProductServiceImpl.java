package com.app.service.impl;

import com.app.dto.InvoiceProductDto;
import com.app.entity.Invoice;
import com.app.entity.InvoiceProduct;
import com.app.repository.InvoiceProductRepository;
import com.app.service.InvoiceProductService;
import com.app.service.InvoiceService;
import com.app.util.MapperUtil;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;


@Service
public class InvoiceProductServiceImpl implements InvoiceProductService {

    private final InvoiceProductRepository invoiceProductRepository;
    private final MapperUtil mapperUtil;
    private final InvoiceService invoiceService;

    public InvoiceProductServiceImpl(InvoiceProductRepository invoiceProductRepository, MapperUtil mapperUtil,@Lazy InvoiceService invoiceService) {
        this.invoiceProductRepository = invoiceProductRepository;
        this.mapperUtil = mapperUtil;
        this.invoiceService = invoiceService;
    }


    @Override
    public InvoiceProductDto findInvoiceProductById(Long id) {
        InvoiceProduct invoiceProduct = invoiceProductRepository.findById(id).orElseThrow(RuntimeException::new);
        return mapperUtil.convert(invoiceProduct, new InvoiceProductDto());
    }

    @Override
    public List<InvoiceProductDto> listAllByInvoiceIdAndCalculateTotalPrice(Long id) {
        return invoiceProductRepository.findAllByInvoiceId(id).stream()
                .map(each -> {
                    InvoiceProductDto invoiceProductDto = mapperUtil.convert(each, new InvoiceProductDto());
                    invoiceProductDto.setTotal(getTotalPriceWithTax(each));
                    return invoiceProductDto;
                })
                .toList();
    }

    @Override
    public void saveInvoiceProduct(Long invoiceId, InvoiceProductDto dto) {
        Invoice invoice = mapperUtil.convert(invoiceService.listInvoiceById(invoiceId), new Invoice());
        InvoiceProduct invoiceProduct = mapperUtil.convert(dto, new InvoiceProduct());
        invoiceProduct.setInvoice(invoice);
        mapperUtil.convert(invoiceProductRepository.save(invoiceProduct), new InvoiceProductDto());
    }

    @Override
    public void deleteInvoiceProduct(Long invoiceProductId) {
        InvoiceProduct invoiceProduct = invoiceProductRepository.findById(invoiceProductId).orElseThrow(RuntimeException::new);
        invoiceProduct.setIsDeleted(true);
        invoiceProductRepository.save(invoiceProduct);
    }

    @Override
    public List<InvoiceProductDto> findInvoiceProductByInvoiceId(Long invoiceId) {
        List<InvoiceProduct> list = invoiceProductRepository.findAllByInvoiceId(invoiceId);
        return list.stream().map(each->mapperUtil.convert(each, new InvoiceProductDto())).toList();
    }

    private BigDecimal getTotalPriceWithTax(InvoiceProduct invoiceProduct) {
        BigDecimal totalPrice= invoiceProduct.getPrice().multiply(BigDecimal.valueOf(invoiceProduct.getQuantity()));
        BigDecimal totalTax= totalPrice.multiply(BigDecimal.valueOf(invoiceProduct.getTax() / 100d));
        return totalPrice.add(totalTax);
    }


}
