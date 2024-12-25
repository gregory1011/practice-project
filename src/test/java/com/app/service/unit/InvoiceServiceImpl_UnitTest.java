package com.app.service.unit;

import com.app.dto.InvoiceDto;
import com.app.dto.InvoiceProductDto;
import com.app.entity.Invoice;
import com.app.entity.InvoiceProduct;
import com.app.enums.CompanyStatus;
import com.app.enums.InvoiceStatus;
import com.app.enums.InvoiceType;
import com.app.exceptions.InvoiceNotFoundException;
import com.app.repository.InvoiceRepository;
import com.app.service.CompanyService;
import com.app.service.InvoiceProductService;
import com.app.service.TestDocInitializer;
import com.app.service.impl.InvoiceServiceImpl;
import com.app.util.MapperUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.ThrowableAssert.catchThrowable;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
public class InvoiceServiceImpl_UnitTest {

    @Mock private InvoiceRepository invoiceRepository;
    @Mock private InvoiceProductService invoiceProductService;
    @Mock private CompanyService companyService;
    @Spy private MapperUtil mapperUtil= new MapperUtil(new ModelMapper());
    @InjectMocks private InvoiceServiceImpl invoiceService;


    private Invoice invoice;
    private InvoiceDto invoiceDto;
    private InvoiceProductDto invoiceProductDto;
    private InvoiceProduct invoiceProduct;
    @BeforeEach
    void setUp() {
        invoiceDto = TestDocInitializer.getInvoiceDto(InvoiceStatus.AWAITING_APPROVAL, InvoiceType.SALES);
        invoice= mapperUtil.convert(invoiceDto, new Invoice());
        invoiceProductDto= TestDocInitializer.getInvoiceProductDto();
        invoiceProduct= mapperUtil.convert(invoiceProductDto, new InvoiceProduct());
    }

    @Test
    @DisplayName("findById_exist")
    void findById_shouldReturnInvoice() {
        //when part
        when(invoiceRepository.findById(anyLong())).thenReturn(Optional.of(invoice));
        InvoiceDto actualResult= invoiceService.findById(anyLong());
        //then
        assertThat(actualResult).usingRecursiveComparison()
                .ignoringFields("price", "tax", "total")
                .isEqualTo(invoiceDto);
    }
    @Test
    @DisplayName("findById_notFound")
    void findById_shouldThrowExceptionIfInvoiceNotFound() {
        // when
        when(invoiceRepository.findById(anyLong())).thenReturn(Optional.empty());
        Throwable throwable = catchThrowable(() -> invoiceService.findById(anyLong()));
        assertThat(throwable).isInstanceOf(InvoiceNotFoundException.class);
    }

    @Test
    void listInvoices_shouldSortAndReturnInvoices() {
        //given
        List<InvoiceDto> dtoList = Arrays.asList(invoiceDto, invoiceDto, invoiceDto);
        List<Invoice> invoices= dtoList.stream().map(each -> mapperUtil.convert(each, new Invoice())).toList();
        List<InvoiceDto> expectedList = dtoList.stream().sorted(Comparator.comparing(InvoiceDto::getPrice).reversed()).toList();
        List<InvoiceProductDto> invoiceProductList = List.of(invoiceProductDto, invoiceProductDto, invoiceProductDto);
        invoiceProductList.stream().map(each->{
            each.setTotal(BigDecimal.valueOf(55));
            return each; }).toList();

        //when part
        when(invoiceRepository.findAllByCompanyIdAndInvoiceType(1L, InvoiceType.SALES)).thenReturn(invoices);
        when(companyService.getCompanyByLoggedInUser()).thenReturn(TestDocInitializer.getCompany(CompanyStatus.ACTIVE));
        when(invoiceProductService.listAllByInvoiceIdAndCalculateTotalPrice(1L)).thenReturn(invoiceProductList);

        List<InvoiceDto> actualResult = invoiceService.listInvoices(InvoiceType.SALES);
        //then
        assertThat(actualResult).usingRecursiveComparison()
                .ignoringExpectedNullFields()
                .ignoringFields("price", "tax", "total")
                .isEqualTo(expectedList);

    }

    @Test
    void should_generate_new_invoice() {
        //when
        when(companyService.getCompanyByLoggedInUser()).thenReturn(TestDocInitializer.getCompany(CompanyStatus.ACTIVE));
        when(invoiceRepository.findFirstByCompanyIdAndInvoiceTypeOrderByInvoiceNoDesc(anyLong(), any(InvoiceType.class))).thenReturn(invoice);

        InvoiceDto result = invoiceService.generateNewInvoiceDto(InvoiceType.SALES);
        //then part
        assertThat(result.getInvoiceNo()).isEqualTo("S-101");

    }
}
