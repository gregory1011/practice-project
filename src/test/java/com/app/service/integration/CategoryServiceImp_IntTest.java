package com.app.service.integration;


import com.app.dto.CategoryDto;
import com.app.entity.Category;
import com.app.exceptions.CategoryNotFoundException;
import com.app.repository.CategoryRepository;
import com.app.service.CategoryService;
import com.app.service.SecuritySetUpUtil;
import com.app.service.TestDocInitializer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.ThrowableAssert.catchThrowable;


@SpringBootTest
public class CategoryServiceImp_IntTest {

    @Autowired
    private CategoryService categoryService;
    @Autowired
    private CategoryRepository categoryRepository;

    @BeforeEach
    void setUp() {
        SecuritySetUpUtil.setUpSecurityContext();
    }

    @Test
    void testFindById() {
        Long id = 1L;
        CategoryDto categoryDto = categoryService.findById(id);
        assertThat(categoryDto).isNotNull();
        assertThat(categoryDto.getId()).isEqualTo(id);
    }

    @Test
    void testFindById_NotFound() {
        Throwable throwable = catchThrowable(() -> categoryService.findById(-1L));
        assertThat(throwable).isInstanceOf(CategoryNotFoundException.class);
        assertThat(throwable.getMessage()).isEqualTo("Category not found");
    }

    @Test
    void testListAllCategories() {

        List<CategoryDto> dtoList = categoryService.listAllCategories();
        assertThat(dtoList).isNotEmpty();
        assertThat(dtoList).isNotNull();
    }

    @Test
    void testSaveCategory() {
        CategoryDto categoryDto = TestDocInitializer.getCategory();
        CategoryDto savedCategory = categoryService.saveCategory(categoryDto);
        assertThat(savedCategory).isNotNull();
        assertThat(savedCategory.getId()).isNotNull();
        assertThat(savedCategory.getDescription()).isEqualTo(categoryDto.getDescription());
    }

    @Test
    void testUpdateCategory() {
        CategoryDto categoryDto = TestDocInitializer.getCategory();
        CategoryDto updatedCategory = categoryService.updateCategory(categoryDto);

        assertThat(updatedCategory).isNotNull();
        assertThat(updatedCategory.getId()).isNotNull();
        assertThat(updatedCategory.getDescription()).isEqualTo(categoryDto.getDescription());
    }

    @Test
    @Transactional(readOnly = true) // we use transactional to handle deleteCategory() -> when the category: isDeleted= true, and we search for id (in entity class @Where(clause = "is_deleted=false")
    void testDeleteById() {
        Long id = 1L;
        categoryService.deleteById(id);
        Category category = categoryRepository.findById(id).orElse(null);
        assertThat(category).isNotNull();
        assertThat(category.getId()).isEqualTo(id);
        assertThat(category.getDescription()).isNotNull();
        assertThat(category.getIsDeleted()).isTrue();

        //negative scenario id= -1; throws exception
        Throwable throwable = catchThrowable(() -> categoryService.findById(-1L));
        assertThat(throwable).isInstanceOf(CategoryNotFoundException.class);
        assertThat(throwable.getMessage()).isEqualTo("Category not found");
    }

}
