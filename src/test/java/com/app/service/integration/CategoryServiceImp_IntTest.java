package com.app.service.integration;


import com.app.dto.CategoryDto;
import com.app.exceptions.CategoryNotFoundException;
import com.app.exceptions.UserNotFoundException;
import com.app.repository.CategoryRepository;
import com.app.service.CategoryService;
import com.app.service.SecuritySetUpUtil;
import com.app.service.TestDocInitializer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.crossstore.ChangeSetPersister;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.ThrowableAssert.catchThrowable;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class CategoryServiceImp_IntTest {

    @Autowired
    private CategoryService categoryService;
    @Autowired
    private CategoryRepository categoryRepository;
//    @Autowired
//    private SecurityService securityService;

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
}
