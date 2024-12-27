package com.app.service.unit;


import com.app.dto.InvoiceProductDto;
import com.app.dto.ProductDto;
import com.app.entity.Invoice;
import com.app.entity.InvoiceProduct;
import com.app.entity.Product;
import com.app.enums.CompanyStatus;
import com.app.exceptions.ProductNotFoundException;
import com.app.repository.ProductRepository;
import com.app.service.CompanyService;
import com.app.service.TestDocInitializer;
import com.app.service.impl.ProductServiceImpl;
import com.app.util.MapperUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.ThrowableAssert.catchThrowable;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;



@ExtendWith(MockitoExtension.class)
public class ProductServiceImpl_UnitTest {


    @Mock private ProductRepository productRepository;
    @Mock private CompanyService companyService;
    @Spy private MapperUtil mapperUtil= new MapperUtil(new ModelMapper());
    @InjectMocks private ProductServiceImpl productService;

    private ProductDto productDto;
    private Product product;
    @BeforeEach
    void setUp() {
        productDto= TestDocInitializer.getProductDto();
        product= mapperUtil.convert(productDto, new Product());
    }

    @Test
    void findById_shouldReturnProduct() {
        when(productRepository.findById(anyLong())).thenReturn(Optional.of(product));
        ProductDto actualResult = productService.findById(anyLong());
        assertEquals(productDto, actualResult);
    }
    @Test
    void findById_shouldThrowException(){
        when(productRepository.findById(anyLong())).thenReturn(Optional.empty());
        Throwable throwable = catchThrowable(() -> productService.findById(anyLong()));
        assertInstanceOf(ProductNotFoundException.class, throwable);
    }

    @Test
    void listAllProducts_shouldReturnProductList() {
        //given
        List<ProductDto> productList = List.of(productDto, productDto, productDto);
        productList.get(0).getCategory().setDescription("samsung");
        productList.get(1).getCategory().setDescription("apple");
        productList.get(2).getCategory().setDescription("apple");
        productList.get(0).setName("Galaxy");
        productList.get(1).setName("Iphone");
        productList.get(2).setName("Macbook Pro");
        List<Product> products = productList.stream().map(each -> {
                    Product entity = mapperUtil.convert(each, new Product());
                    entity.setInvoiceProducts(List.of(new InvoiceProduct()));
                    return entity;
                }).toList();

        List<ProductDto> expectedList = productList.stream().sorted(Comparator.comparing((ProductDto p) -> p.getCategory().getDescription())
                .thenComparing(ProductDto::getName)).toList();

        //when part
        when(companyService.getCompanyByLoggedInUser()).thenReturn(TestDocInitializer.getCompany(CompanyStatus.ACTIVE));
        when(productRepository.findByCompanyId(anyLong())).thenReturn(products);

        List<ProductDto> actualList = productService.listAllProducts();
        //then part
        assertThat(actualList).usingRecursiveComparison()
                .ignoringExpectedNullFields()
                .ignoringFields("hasInvoiceProduct")
                .isEqualTo(expectedList);
    }

    @Test
    void save_shouldReturnSavedProduct() {
        //when
        when(companyService.getCompanyByLoggedInUser()).thenReturn(TestDocInitializer.getCompany(CompanyStatus.ACTIVE));
        when(productRepository.save(any())).thenReturn(product);

        ProductDto actualResult= productService.saveProduct(productDto);
        //then part
        assertThat(actualResult).usingRecursiveComparison()
                .ignoringExpectedNullFields()
                .ignoringFields("hasInvoiceProduct")
                .isEqualTo(productDto);
    }

    @Test
//    @Transactional
    void deleteById_shouldSetIsDeletedTrue() {
        //given
        product.setId(1L);
        product.setQuantityInStock(0);
        product.setInvoiceProducts(new ArrayList<>());
        product.setIsDeleted(false);

        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        when(productRepository.save(any())).thenReturn(product);

        productService.deleteProductById(1L);
        //assert
        verify(productRepository, times(1)).save(product);
        assertTrue(product.getIsDeleted());
    }
    @Test
    void deleteById_shouldThrowExceptionIfProductNotExists() {
        when(productRepository.findById(1L)).thenReturn(Optional.empty());
        Throwable throwable = catchThrowable(() -> productService.deleteProductById(1L));
        assertInstanceOf(ProductNotFoundException.class, throwable);
    }

    @Test
    void update_shouldReturnUpdatedProduct() {
        //when
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        when(productRepository.save(any())).thenReturn(product);

        ProductDto actualResult = productService.updateProduct(productDto);
        //assert
        assertThat(actualResult).usingRecursiveComparison()
                .ignoringExpectedNullFields()
                .ignoringFields("hasInvoiceProduct")
                .isEqualTo(productDto);
        assertThat(actualResult.getQuantityInStock()).isNotNull();
    }

    @Test
    void isProductNameExist() {
    }
}
