package com.app.service.unit;


import com.app.dto.CategoryDto;
import com.app.dto.CompanyDto;
import com.app.dto.UserDto;
import com.app.entity.Category;
import com.app.entity.Company;
import com.app.entity.Product;
import com.app.enums.CompanyStatus;
import com.app.exceptions.CategoryNotFoundException;
import com.app.repository.CategoryRepository;
import com.app.service.SecurityService;
import com.app.service.TestDocInitializer;
import com.app.service.impl.CategoryServiceImpl;
import com.app.util.MapperUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;



@ExtendWith(MockitoExtension.class)
public class CategoryServiceImpl_UnitTest {


    @Mock private CategoryRepository categoryRepository;
    @Mock private SecurityService securityService;
    @Spy private MapperUtil mapperUtil= new MapperUtil(new ModelMapper());
    @InjectMocks private CategoryServiceImpl categoryService;

    private Category category;
    private CategoryDto categoryDto;

    @BeforeEach
    void setUp() {
        CompanyDto companyDto = new CompanyDto();
        companyDto.setId(1L);

        category= new Category();
        category.setId(1L);
        category.setDescription("Test Category");
        category.setCompany(mapperUtil.convert(companyDto, new Company()));
        category.setProduct(List.of(new Product()));

        categoryDto= new CategoryDto();
        categoryDto.setId(1L);
        categoryDto.setDescription("Test Category");
        categoryDto.setCompany(companyDto);
        categoryDto.setHasProduct(true);
    }


    @Test
    @DisplayName(value = "findById")
    void findById_shouldReturnCategoryDto(){
        when(categoryRepository.findById(anyLong())).thenReturn(Optional.of(category));
        CategoryDto result = categoryService.findById(1L);

        assertThat(result).usingRecursiveComparison().isEqualTo(categoryDto);
        assertThat(result.isHasProduct()).isTrue();
        verify(categoryRepository, times(1)).findById(anyLong());
    }
    @Test
    @DisplayName("0x1F600")
    void findById_shouldThrowException(){
        when(categoryRepository.findById(anyLong())).thenReturn(Optional.empty());
        assertThrows(CategoryNotFoundException.class, () -> categoryService.findById(1L));
    }

    @Test
    void findAllCategories_shouldReturnListOfCategoryDto(){
        //given
        CompanyDto companyDto = TestDocInitializer.getCompany(CompanyStatus.ACTIVE);
        UserDto loggedInUser = TestDocInitializer.getUser("Admin");
        ArrayList<Category> list = new ArrayList<>();
        list.add(category);
        //when
        when(securityService.getLoggedInUser()).thenReturn(loggedInUser);
        when(categoryRepository.findByCompanyId(companyDto.getId())).thenReturn(list);

        List<CategoryDto> resultList = categoryService.listAllCategories();

        assertThat(resultList).hasSize(1);
        assertThat(resultList.get(0)).usingRecursiveComparison().isEqualTo(categoryDto);
    }

    @Test
    void saveCategory_shouldReturnSavedCategoryDto(){
        //given
        UserDto loggedInUser = TestDocInitializer.getUser("Admin");

        //when
        when(securityService.getLoggedInUser()).thenReturn(loggedInUser);
        when(categoryRepository.save(any(Category.class))).thenReturn(category);

        CategoryDto result = categoryService.saveCategory(categoryDto);
        assertThat(result).usingRecursiveComparison()
                .ignoringFields("hasProduct")
                .isEqualTo(categoryDto);
    }

    @Test
    void isDescriptionExists_shouldReturnTrueIfDescriptionExist(){
        //given
        UserDto loggedInUser = TestDocInitializer.getUser("Admin");

        when(securityService.getLoggedInUser()).thenReturn(loggedInUser);
        when(categoryRepository.findCategoryByDescriptionAndCompanyId(anyString(), anyLong())).thenReturn(category);

        boolean descriptionExists = categoryService.isDescriptionExists(categoryDto);
        assertThat(descriptionExists).isTrue();
    }
    @Test
    void isDescriptionExists_shouldReturnFalseIfDescriptionNotExist(){
        //given
        UserDto loggedInUser = TestDocInitializer.getUser("Admin");

        when(securityService.getLoggedInUser()).thenReturn(loggedInUser);
        when(categoryRepository.findCategoryByDescriptionAndCompanyId(anyString(), anyLong())).thenReturn(null);

        boolean descriptionExists = categoryService.isDescriptionExists(categoryDto);
        assertThat(descriptionExists).isFalse();
    }

    @Test
    void updateCategory_shouldReturnUpdatedCategoryDto(){

        when(categoryRepository.findById(anyLong())).thenReturn(Optional.of(category));
        when(categoryRepository.save(any(Category.class))).thenReturn(category);

        CategoryDto result = categoryService.updateCategory(categoryDto);
        assertThat(result).usingRecursiveComparison()
                .ignoringFields("hasProduct")
                .isEqualTo(categoryDto);
    }
    @Test
    void updateCategory_shouldThrowException(){
        when(categoryRepository.findById(anyLong())).thenReturn(Optional.empty());
        assertThrows(CategoryNotFoundException.class, () -> categoryService.updateCategory(categoryDto));
    }

    @Test
    void deleteCategory_shouldMarkCategoryAsDeleted(){
        // when
        when(categoryRepository.findById(anyLong())).thenReturn(Optional.of(category));
        categoryService.deleteById(1L);
        assertThat(category.getIsDeleted()).isTrue();
        assertThat(category.getDescription()).isEqualTo("Test Category-1");
    }

    @Test
    void deleteCategory_shouldThrowException(){
        when(categoryRepository.findById(anyLong())).thenReturn(Optional.empty());
        Throwable throwable  = catchThrowable(() -> categoryService.findById(1L));
        assertThat(throwable).isInstanceOf(CategoryNotFoundException.class);
        assertThat(category.getIsDeleted()).isFalse();
    }

    @Test
    void name() {
        IntStream.rangeClosed(2, 100)
                .filter( i -> i % 2 == 0 );
    }
}
