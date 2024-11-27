package com.app.repository;

import com.app.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {

    List<Category> findByCompanyId(Long companyId);
    @Query(value = "select c from categories c where c.description= ?1 and c.company.id= ?2")
    Category findCategoryByDescriptionAndCompanyId (String description, Long companyId);

}
