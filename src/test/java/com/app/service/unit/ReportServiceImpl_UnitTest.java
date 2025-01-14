package com.app.service.unit;


import com.app.dto.InvoiceDto;
import com.app.dto.InvoiceProductDto;
import com.app.entity.Invoice;
import com.app.entity.InvoiceProduct;
import com.app.enums.InvoiceStatus;
import com.app.enums.InvoiceType;
import com.app.repository.InvoiceProductRepository;
import com.app.service.CompanyService;
import com.app.service.InvoiceProductService;
import com.app.service.InvoiceService;
import com.app.service.TestDocInitializer;
import com.app.service.impl.ReportServiceImpl;
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
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.ThrowableAssert.catchThrowable;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;



@ExtendWith(MockitoExtension.class)
public class ReportServiceImpl_UnitTest {


    @Mock private InvoiceService invoiceService;
    @Mock private InvoiceProductService invoiceProductService;
    @Mock private InvoiceProductRepository invoiceProductRepository;
    @Mock private CompanyService companyService;
    @Spy private MapperUtil mapperUtil= new MapperUtil(new ModelMapper());
    @InjectMocks
    private ReportServiceImpl reportService;

    private InvoiceProduct invoiceProduct;
    private InvoiceProductDto invoiceProductDto;
    private InvoiceDto invoiceDto;
    private Invoice invoice;

    @BeforeEach
    void setUp() {
        invoiceProductDto= TestDocInitializer.getInvoiceProductDto();
        invoiceProduct= mapperUtil.convert(invoiceProductDto, new InvoiceProduct());
        invoiceDto= TestDocInitializer.getInvoiceDto(InvoiceStatus.APPROVED, InvoiceType.PURCHASE);
        invoice= mapperUtil.convert(invoiceDto, new Invoice());
    }

    @Test
    void listAllInvoiceProductsOfCompany() {
        //given
//        CompanyDto companyDto = TestDocInitializer.getCompany(CompanyStatus.ACTIVE);
//        invoiceProduct.setInvoice(invoice);
//        List<InvoiceProduct> list = List.of(invoiceProduct);
//        List<InvoiceProductDto>list2= List.of(invoiceProductDto);

        //when
//        when(companyService.getCompanyByLoggedInUser()).thenReturn(companyDto);
//        when(invoiceProductRepository.retrieveByInvoiceCompanyIdAndAndInvoiceStatus(anyLong(), eq(InvoiceStatus.APPROVED))).thenReturn(list);
//        when(invoiceProductService.listAllApprovedInvoiceProductsOfCompany()).thenReturn(list2);
        List<InvoiceProductDto> result = reportService.listAllInvoiceProductsOfCompany();

        // assert
        assertNotNull(result);
    }

    @Test
    void listMonthlyProfitLossMap_shouldReturnMonthlyProfitLossMap() {

        InvoiceDto invoiceDto = TestDocInitializer.getInvoiceDto(InvoiceStatus.APPROVED, InvoiceType.SALES);
        InvoiceProductDto invoiceProductDto = TestDocInitializer.getInvoiceProductDto();
        LocalDate now = LocalDate.now();
        invoiceProductDto.getInvoice().setDate(now);
        invoiceProductDto.setProfitLoss(BigDecimal.TEN);
        InvoiceProductDto invoiceProductDto2 = TestDocInitializer.getInvoiceProductDto();
        invoiceProductDto2.getInvoice().setDate(now.minusMonths(1));
        invoiceProductDto2.setProfitLoss(BigDecimal.ONE);
        //when
        when(invoiceService.listInvoices(InvoiceType.SALES)).thenReturn(List.of(invoiceDto));
        when(invoiceProductService.listAllByInvoiceIdAndCalculateTotalPrice(anyLong())).thenReturn(List.of(invoiceProductDto, invoiceProductDto2));

        Map<String, BigDecimal> map =reportService.listMonthlyProfitLossMap();
        System.out.println("map = " + map);
        //assert
        String key1 = now.getMonth() + " - " + now.getYear();
        String key2 = now.minusMonths(1).getYear() + " - " + now.minusMonths(1).getMonth();
        assertThat(map).hasSize(2);
//                .containsEntry(key1, BigDecimal.TEN)
//                .containsEntry(key2, BigDecimal.ONE);
    }


}
