package com.app.repository;

import com.app.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {

    boolean existsAllByDescriptionAndCompanyId(String name, Long companyId);
}
