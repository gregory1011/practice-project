package com.app.service.unit;


import com.app.dto.InvoiceDto;
import com.app.dto.InvoiceProductDto;
import com.app.entity.InvoiceProduct;
import com.app.exceptions.InvoiceProductNotFoundException;
import com.app.repository.InvoiceProductRepository;
import com.app.service.CompanyService;
import com.app.service.ProductService;
import com.app.service.TestDocInitializer;
import com.app.service.impl.InvoiceProductServiceImpl;
import com.app.util.MapperUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
public class InvoiceProductServiceImpl_UnitTest {

    @Mock
    private InvoiceProductRepository invoiceProductRepository;
    @Mock
    private ProductService productService;
    @Mock
    private CompanyService companyService;
    @Spy
    private MapperUtil mapperUtil= new MapperUtil(new ModelMapper());
    @InjectMocks
    private InvoiceProductServiceImpl invoiceProductService;

    private InvoiceProduct invoiceProduct;
    private InvoiceProductDto invoiceProductDto;
    @BeforeEach
    void setUp() {
        invoiceProductDto= TestDocInitializer.getInvoiceProductDto();
        invoiceProduct= mapperUtil.convert(invoiceProductDto, new InvoiceProduct());
    }

    @Test
    void findById_shouldReturnInvoiceProductDto() {
        when(invoiceProductRepository.findById(anyLong())).thenReturn(Optional.of(invoiceProduct));
        InvoiceProductDto result= invoiceProductService.findById(anyLong());
        assertNotNull(result);
        assertThat(result).usingRecursiveComparison()
                .ignoringFields("invoice.price", "invoice.tax", "remainingQuantity")
                .isEqualTo(invoiceProductDto);
    }
    @Test
    void findById_shouldThrowException() {
        when(invoiceProductRepository.findById(anyLong())).thenReturn(Optional.empty());
        Throwable throwable = catchThrowable(() -> invoiceProductService.findById(anyLong()));
        assertThat(throwable).isInstanceOf(InvoiceProductNotFoundException.class);
    }

    @Test
    void listAllByInvoiceIdAndCalculateTotalPrice() {
        //given
        List<InvoiceProductDto> list = List.of(invoiceProductDto);
        List<InvoiceProductDto> expectedList = list.stream().peek(each -> each.setTotal(each.getPrice().multiply( BigDecimal.valueOf(each.getQuantity() * (each.getTax() + 100d) / 100d)))).toList();

        //when
        when(invoiceProductRepository.findAllByInvoiceId(anyLong())).thenReturn(List.of(invoiceProduct));

        List<InvoiceProductDto> actualList = invoiceProductService.listAllByInvoiceIdAndCalculateTotalPrice(1L);

         //then
        assertThat(actualList).usingRecursiveComparison()
                .ignoringExpectedNullFields()
                .isEqualTo(expectedList);
    }


}
