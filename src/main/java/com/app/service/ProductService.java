package com.app.service;

import com.app.dto.ProductDto;

import java.util.List;

public interface ProductService {

    ProductDto listProductById(Long id);
    List<ProductDto> listAllProducts();
    void saveProduct(ProductDto dto);
    void deleteProductById(Long id);
    void updateProduct(ProductDto dto);
}
