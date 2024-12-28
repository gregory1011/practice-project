package com.app.service.impl;

import com.app.dto.CategoryDto;
import com.app.dto.CompanyDto;
import com.app.entity.Category;
import com.app.exceptions.CategoryNotFoundException;
import com.app.repository.CategoryRepository;
import com.app.service.CategoryService;
import com.app.service.SecurityService;
import com.app.util.MapperUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;


@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;
    private final MapperUtil mapperUtil;
    private final SecurityService securityService;

    @Override
    public CategoryDto findById(Long id) {
        Category category = categoryRepository.findById(id).orElseThrow(CategoryNotFoundException::new);
        CategoryDto dto = mapperUtil.convert(category, new CategoryDto());
        dto.setHasProduct(!category.getProduct().isEmpty()); // if is not empty= true, otherwise= false
        return dto;
    }

    @Override
    public List<CategoryDto> listAllCategories() {
        CompanyDto companyDto = securityService.getLoggedInUser().getCompany();
        return categoryRepository.findByCompanyId(companyDto.getId()).stream()
                .sorted(Comparator.comparing(Category::getDescription))
                .map(each-> {  // block of code to determine if it hasProducts true or false;
                    CategoryDto dto = mapperUtil.convert(each, new CategoryDto());
                    boolean change= false;
                    change= !each.getProduct().isEmpty(); // if it has product then it's true;
                    dto.setHasProduct(change);
                    return dto;
                }).toList();
    }

    @Override
    public CategoryDto saveCategory(CategoryDto dto) {
        CompanyDto company = securityService.getLoggedInUser().getCompany();
        dto.setCompany(company);
        Category category = mapperUtil.convert(dto, new Category());
        categoryRepository.save(category);
        return mapperUtil.convert(category, new CategoryDto());
    }

    @Override
    public CategoryDto updateCategory(CategoryDto dto) {
        Category category = categoryRepository.findById(dto.getId()).orElseThrow(CategoryNotFoundException::new);
        category.setDescription(dto.getDescription());
        categoryRepository.save(category);
        return mapperUtil.convert(category, new CategoryDto());
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
