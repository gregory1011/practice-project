package com.app.service.impl;

import com.app.dto.CompanyDto;
import com.app.dto.ProductDto;
import com.app.entity.Product;
import com.app.exceptions.ProductNotFoundException;
import com.app.repository.ProductRepository;
import com.app.service.CompanyService;
import com.app.service.ProductService;
import com.app.util.MapperUtil;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;


@Service
@AllArgsConstructor
public class ProductServiceImpl implements ProductService {


    private final ProductRepository productRepository;
    private final MapperUtil mapperUtil;
    private final CompanyService companyService;


    @Override
    public ProductDto findById(Long id) {
        Product product = productRepository.findById(id).orElseThrow(ProductNotFoundException::new);
        return mapperUtil.convert(product, new ProductDto());
    }

    @Override
    public List<ProductDto> listAllProducts() {
        Long companyId = companyService.getCompanyByLoggedInUser().getId();
        return productRepository.findByCompanyId(companyId).stream()
                .sorted(Comparator.comparing((Product p) -> p.getCategory().getDescription())
                        .thenComparing(Product::getName))
                .map(each -> {
                    ProductDto dto = mapperUtil.convert(each, new ProductDto());
                    dto.setHasInvoiceProduct(!each.getInvoiceProducts().isEmpty());
                    return dto;
                }).toList();
    }

    @Override
    public ProductDto saveProduct(ProductDto dto) {
        CompanyDto company = companyService.getCompanyByLoggedInUser();
        dto.getCategory().setCompany(company);
        Product product = mapperUtil.convert(dto, new Product());
        Product saved = productRepository.save(product);
        return mapperUtil.convert(saved, new ProductDto());
    }

    @Override
    public void deleteProductById(Long id) {
        Product product = productRepository.findById(id).orElseThrow(ProductNotFoundException::new);
        if(product.getQuantityInStock() == 0 && product.getInvoiceProducts().isEmpty()){
            product.setIsDeleted(true);
            productRepository.save(product);
        }
    }

    @Override
    public ProductDto updateProduct(ProductDto dto) {
        Product product = productRepository.findById(dto.getId()).orElseThrow(ProductNotFoundException::new);
        final int quantityInStock= dto.getQuantityInStock() == null ? product.getQuantityInStock() : dto.getQuantityInStock();
        dto.setQuantityInStock(quantityInStock);
        Product entity= mapperUtil.convert(dto, new Product());
        Product saved = productRepository.save(entity);
        return mapperUtil.convert(saved, new ProductDto());
    }

    @Override
    public boolean isProductNameExists(ProductDto dto) {
        Product product= productRepository.findProductByNameAndCompanyId(dto.getName(), companyService.getCompanyByLoggedInUser().getId());
        if (product == null) return false; // false
        return !Objects.equals(product.getId(), dto.getId());//false // dto id= null, it's a new dto obj without id. it hasn't been saved in DB therefore id= null
    }

}
