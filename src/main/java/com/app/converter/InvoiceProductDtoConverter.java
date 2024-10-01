package com.app.converter;

import com.app.dto.InvoiceProductDto;
import com.app.service.InvoiceProductService;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;


@Component
public class InvoiceProductDtoConverter implements Converter<String, InvoiceProductDto> {

    private final InvoiceProductService invoiceProductService;

    public InvoiceProductDtoConverter(InvoiceProductService invoiceProductService) {
        this.invoiceProductService = invoiceProductService;
    }

    @Override
    public InvoiceProductDto convert(String source) {
        return invoiceProductService.findInvoiceProductById(Long.parseLong(source));
    }


}
