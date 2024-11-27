package com.app.service.integration;


import com.app.repository.CategoryRepository;
import com.app.service.CategoryService;
import com.app.service.SecurityService;
import com.app.service.SecuritySetUpUtil;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;


@SpringBootTest
public class CategoryServiceImp_IntTest {

    @Autowired
    private CategoryService categoryService;
    @Autowired
    private CategoryRepository categoryRepository;
//    @Autowired
//    private SecurityService securityService;

    @BeforeEach
    void setUp() {
        SecuritySetUpUtil.setUpSecurityContext();
    }



}
