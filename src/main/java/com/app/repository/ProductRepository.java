package com.app.repository;

import com.app.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    @Query("select p from products  p where p.category.company.id= ?1")
    List<Product> findByCompanyId(Long companyId);

    @Query(value = "SELECT p FROM products p WHERE p.name= ?1 AND p.category.company.id= ?2")
    Product findProductByNameAndCompanyId(String name, Long companyId);

}

