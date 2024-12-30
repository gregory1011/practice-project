package com.app.service.integration;


import com.app.dto.ProductDto;
import com.app.entity.Product;
import com.app.exceptions.ProductNotFoundException;
import com.app.repository.ProductRepository;
import com.app.service.SecuritySetUpUtil;
import com.app.service.TestDocInitializer;
import com.app.service.impl.ProductServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.ThrowableAssert.catchThrowable;
import static org.junit.jupiter.api.Assertions.*;



@SpringBootTest
public class ProductServiceImpl_IntTest {

    @Autowired private ProductServiceImpl productService;
    @Autowired private ProductRepository productRepository;

    @BeforeEach
    void setUp() {
        SecuritySetUpUtil.setUpSecurityContext();
    }

    @Test
    void test_findById() {
        ProductDto result = productService.findById(1L);
        assertNotNull(result);
    }
    @Test
    void test_findById_throwException() {
        Throwable throwable = catchThrowable(() -> productService.findById(-1L));
        assertThat(throwable).isInstanceOf(ProductNotFoundException.class);
    }

    @Test
    @Transactional
    void test_listAllProducts() {
        List<ProductDto> result= productService.listAllProducts();
        assertNotNull(result);
        assertFalse(result.isEmpty());
        ProductDto productDto = productService.findById(2L);
        assertThat(result.get(0)).usingRecursiveComparison()
                .ignoringFields("id", "hasInvoiceProduct")
                .isEqualTo(productDto);
    }

    @Test
    void test_save() {
        ProductDto productDto = TestDocInitializer.getProductDto();
        ProductDto result = productService.saveProduct(productDto);
        assertNotNull(result);
        assertEquals(result.getName(), productDto.getName());
    }

    @Test
    void test_update() {
        ProductDto productDto = TestDocInitializer.getProductDto();
        productDto.setName("Updated Product Name");
        ProductDto result = productService.updateProduct(productDto);
        assertNotNull(result);
        assertEquals(result.getName(), productDto.getName());
    }

    @Test
    @Transactional(readOnly = true)
    void test_delete() {
        Product product = productRepository.findById(1L).orElseThrow();
        product.setQuantityInStock(0);
        product.setIsDeleted(false);
        product.setInvoiceProducts(new ArrayList<>());
        productService.deleteProductById(1L);
        Product result = productRepository.findById(1L).orElseThrow();
        assertTrue(result.getIsDeleted());
    }

    @Test
    void test_isProductNameExists() {
        ProductDto productDto = productService.findById(2L);
        boolean result = productService.isProductNameExists(productDto);
        assertFalse(result);
    }
    @Test
    void test_isProductNameNotExists() {
        boolean productNameExists = productService.isProductNameExists(new ProductDto());
        assertFalse(productNameExists);
    }
}
