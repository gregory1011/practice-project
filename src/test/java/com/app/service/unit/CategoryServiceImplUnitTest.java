package com.app.service.unit;


import com.app.repository.CategoryRepository;
import com.app.service.SecurityService;
import com.app.util.MapperUtil;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;


@ExtendWith(MockitoExtension.class)
public class CategoryServiceImplUnitTest {

    @Mock
    private CategoryRepository categoryRepository;
    @Mock
    private SecurityService securityService;
    @Spy
    private MapperUtil mapperUtil= new MapperUtil(new ModelMapper());

}
