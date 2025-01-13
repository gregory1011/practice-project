package com.app.service.unit;


import com.app.dto.CompanyDto;
import com.app.dto.InvoiceDto;
import com.app.dto.InvoiceProductDto;
import com.app.entity.Invoice;
import com.app.entity.InvoiceProduct;
import com.app.enums.CompanyStatus;
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

import java.util.List;

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
    private ReportServiceImpl reportServiceImpl;

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

        List<InvoiceProductDto> result = reportServiceImpl.listAllInvoiceProductsOfCompany();

        // assert
//        assertNotNull(list);
        assertNotNull(result);
//        assertThat(result.get(0).getInvoice().getInvoiceStatus())
//                .usingRecursiveComparison()
//                .isEqualTo(list.get(0).getInvoice().getInvoiceStatus());
    }

    @Test
    void listMonthlyProfitLossMap_shouldReturnMonthlyProfitLossMap() {

    }


}
