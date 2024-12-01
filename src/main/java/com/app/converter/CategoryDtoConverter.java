package com.app.converter;

import com.app.dto.CategoryDto;
import com.app.service.CategoryService;
import lombok.AllArgsConstructor;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class CategoryDtoConverter implements Converter<String, CategoryDto> {

    private final CategoryService categoryService;

    @Override
    public CategoryDto convert(String source) {
        if (source.isEmpty()) return null;
        return categoryService.findById(Long.parseLong(source));
    }

}
