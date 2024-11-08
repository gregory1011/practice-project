package com.app.converter;

import com.app.dto.ClientVendorDto;
import com.app.service.ClientVendorService;
import lombok.AllArgsConstructor;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;


@Component
@AllArgsConstructor
public class ClientVendorDtoConverter implements Converter<String, ClientVendorDto> {

    private final ClientVendorService clientVendorService;

    @Override
    public ClientVendorDto convert(String source) {
        if (source.isEmpty()) return null;
        return clientVendorService.listClientVendorById(Long.parseLong(source));
    }
}
