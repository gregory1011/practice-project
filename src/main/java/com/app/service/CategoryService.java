package com.app.service;

import com.app.dto.CategoryDto;

import java.util.List;

public interface CategoryService {

    List<CategoryDto> listAllCategories();
    void saveCategory(CategoryDto dto);
    CategoryDto getCategoryById(Long id);
    void updateCategory(Long id, CategoryDto dto);
    boolean isDescriptionExists(String description);
    void deleteById(Long id);
}
