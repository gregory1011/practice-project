package com.app.service.impl;

import com.app.dto.ProductDto;
import com.app.entity.Product;
import com.app.repository.ProductRepository;
import com.app.service.ProductService;
import com.app.service.SecurityService;
import com.app.util.MapperUtil;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;



@Service
@AllArgsConstructor
public class ProductServiceImpl implements ProductService {


    private final ProductRepository productRepository;
    private final MapperUtil mapperUtil;
    private final SecurityService securityService;


    @Override
    public ProductDto listProductById(Long id) {
        Product product = productRepository.findById(id).orElseThrow(RuntimeException::new);
        return mapperUtil.convert(product, new ProductDto());
    }

    @Override
    public List<ProductDto> listAllProducts() {
        Long id = securityService.getLoggedInUser().getCompany().getId();
        return productRepository.findAll().stream()
                .filter(m->m.getCategory().getCompany().getId().equals(id))
                .sorted(Comparator.comparing(m->m.getCategory().getDescription()))
                .sorted(Comparator.comparing(Product::getName))
                .map(each -> {
                    ProductDto dto = mapperUtil.convert(each, new ProductDto());
                    dto.setHasInvoiceProduct(!each.getInvoiceProducts().isEmpty());
                    return dto;
                }).toList();
    }

    @Override
    public void saveProduct(ProductDto dto) {
        Product product = mapperUtil.convert(dto, new Product());
        productRepository.save(product);
    }

    @Override
    public void deleteProductById(Long id) {
        Product product = productRepository.findById(id).orElseThrow(RuntimeException::new);
        if(product.getQuantityInStock() == 0 && product.getInvoiceProducts().isEmpty()){
            product.setIsDeleted(true);
            productRepository.save(product);
        }
    }

    @Override
    public void updateProduct(ProductDto dto) {
        Product product = mapperUtil.convert(dto, new Product());
        productRepository.save(product);
    }

}