package com.app.converter;

import com.app.dto.InvoiceProductDto;
import com.app.service.InvoiceProductService;
import lombok.AllArgsConstructor;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;


@Component
@AllArgsConstructor
public class InvoiceProductDtoConverter implements Converter<String, InvoiceProductDto> {

    private final InvoiceProductService invoiceProductService;

    @Override
    public InvoiceProductDto convert(String source) {
        if (source.isEmpty()) return null;
        return invoiceProductService.findById(Long.parseLong(source));
    }


}
