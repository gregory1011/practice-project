package com.app.service.impl;

import com.app.dto.CategoryDto;
import com.app.dto.CompanyDto;
import com.app.entity.Category;
import com.app.exceptions.CategoryNotFoundException;
import com.app.repository.CategoryRepository;
import com.app.service.CategoryService;
import com.app.service.SecurityService;
import com.app.util.MapperUtil;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;

@Service
@AllArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;
    private final MapperUtil mapperUtil;
    private final SecurityService securityService;

    @Override
    public List<CategoryDto> listAllCategories() {
        Long id = securityService.getLoggedInUser().getCompany().getId();
        List<Category> list = categoryRepository.findAll();
        return list.stream()
                .filter(category -> category.getCompany().getId().equals(id))
                .sorted(Comparator.comparing(Category::getDescription))
                .map(each-> {  // block of code t determine if it hasProducts true or false;
                    CategoryDto dto = mapperUtil.convert(each, new CategoryDto());
                    dto.setHasProduct(!each.getProduct().isEmpty());
                    return dto;
                })
                .toList();
    }

    @Override
    public void saveCategory(CategoryDto dto) {
        CompanyDto company = securityService.getLoggedInUser().getCompany();
        dto.setCompany(company);
        categoryRepository.save(mapperUtil.convert(dto, new Category()));
    }

    @Override
    public CategoryDto getCategoryById(Long id) {
        Category category = categoryRepository.findById(id).orElseThrow(CategoryNotFoundException::new);
        CategoryDto dto = mapperUtil.convert(category, new CategoryDto());
        dto.setHasProduct(!category.getProduct().isEmpty());
        return dto;
    }

    @Override
    public void updateCategory(Long id, CategoryDto dto) {
        dto.setId(id);
        Category category = categoryRepository.findById(id).orElseThrow(CategoryNotFoundException::new);
        category.setDescription(dto.getDescription());
        categoryRepository.save(category);
    }

    @Override
    public boolean isDescriptionExists(CategoryDto dto) {
        Category category = categoryRepository.findCategoryByDescriptionAndCompanyId(dto.getDescription(), securityService.getLoggedInUser().getCompany().getId());
        if (category == null) return false;
        return category.getDescription().equals(dto.getDescription());
    }

    @Override
    public void deleteById(Long id) {
        Category category = categoryRepository.findById(id).orElseThrow(CategoryNotFoundException::new);
        category.setIsDeleted(true);
        category.setDescription(category.getDescription() + "-"+category.getId());
        categoryRepository.save(category);
    }


}
