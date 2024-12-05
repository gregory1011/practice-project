package com.app.service;

import com.app.dto.CategoryDto;
import java.util.List;


public interface CategoryService {

    CategoryDto findById(Long id);
    List<CategoryDto> listAllCategories();
    CategoryDto saveCategory(CategoryDto dto);
    CategoryDto updateCategory(CategoryDto dto);
    boolean isDescriptionExists(CategoryDto dto);
    void deleteById(Long id);
}
