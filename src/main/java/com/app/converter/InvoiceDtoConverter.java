package com.app.converter;

import com.app.dto.InvoiceDto;
import com.app.service.InvoiceService;
import lombok.AllArgsConstructor;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;


@Component
@AllArgsConstructor
public class InvoiceDtoConverter implements Converter<String, InvoiceDto> {

    private final InvoiceService invoiceService;

    @Override
    public InvoiceDto convert(String source) {
        if(source.isEmpty()) return null;
        return invoiceService.listInvoiceById(Long.parseLong(source));
    }

}
