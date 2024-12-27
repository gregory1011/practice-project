package com.app.service;

import com.app.dto.ProductDto;
import com.app.entity.Product;

import java.util.List;

public interface ProductService {

    ProductDto findById(Long id);
    List<ProductDto> listAllProducts();
    ProductDto saveProduct(ProductDto dto);
    ProductDto updateProduct(ProductDto dto);
    void deleteProductById(Long id);
    boolean isProductNameExists(ProductDto dto);
}
