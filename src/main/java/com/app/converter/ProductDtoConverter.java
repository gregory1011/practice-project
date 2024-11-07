package com.app.converter;

import com.app.dto.ProductDto;
import com.app.service.ProductService;
import lombok.AllArgsConstructor;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;


@Component
@AllArgsConstructor
public class ProductDtoConverter implements Converter<String , ProductDto> {

    private final ProductService productService;

    @Override
    public ProductDto convert(String source) {
        if (source.isEmpty()) return null;
        return productService.listProductById(Long.parseLong(source));
    }

}
