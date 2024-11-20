package com.app.service;

import com.app.dto.CategoryDto;

import java.util.List;

public interface CategoryService {

    List<CategoryDto> listAllCategories();
    CategoryDto saveCategory(CategoryDto dto);
    CategoryDto getCategoryById(Long id);
    CategoryDto updateCategory(Long id, CategoryDto dto);
    boolean isDescriptionExists(CategoryDto dto);
    void deleteById(Long id);
}
