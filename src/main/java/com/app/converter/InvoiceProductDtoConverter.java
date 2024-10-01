package com.app.converter;

import com.app.dto.InvoiceProductDto;
import com.app.service.InvoiceProductService;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;


@Component
public class InvoiceProductDtoConverter implements Converter<String, InvoiceProductDto> {

    private final InvoiceProductService invoiceProductService;

    public InvoiceProductDtoConverter(@Lazy InvoiceProductService invoiceProductService) {
        this.invoiceProductService = invoiceProductService;
    }

    @Override
    public InvoiceProductDto convert(String source) {
        if (source.isEmpty()) return null;
        return invoiceProductService.findInvoiceProductById(Long.parseLong(source));
    }


}
