package com.app.service.integration;


import com.app.dto.ClientVendorDto;
import com.app.dto.InvoiceDto;
import com.app.dto.ProductDto;
import com.app.entity.Invoice;
import com.app.enums.ClientVendorType;
import com.app.enums.InvoiceStatus;
import com.app.enums.InvoiceType;
import com.app.exceptions.InvoiceNotFoundException;
import com.app.exceptions.ProductNotFoundException;
import com.app.repository.InvoiceRepository;
import com.app.service.*;
import org.checkerframework.checker.units.qual.A;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.NoSuchElementException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.ThrowableAssert.catchThrowable;
import static org.junit.jupiter.api.Assertions.*;

//@ActiveProfiles("test")
@SpringBootTest
public class InvoiceServiceImpl_IntTest {

    @Autowired
    private InvoiceService invoiceService;
    @Autowired
    private ProductService productService;
    @Autowired
    private InvoiceRepository invoiceRepository;

    @BeforeEach
    void setUp() {
        SecuritySetUpUtil.setUpSecurityContext();
    }


    @Test
    void test_findById() {
        InvoiceDto result = invoiceService.findById(1L);
        assertThat(result).isNotNull();
    }
    @Test
    void test_findById_throwException() {
        Throwable throwable = catchThrowable(() -> invoiceService.findById(-10L));
        assertThat(throwable).isInstanceOf(InvoiceNotFoundException.class);
    }

    @Test
    void test_listPurchaseInvoices() {
        List<InvoiceDto> actualList = invoiceService.listInvoices(InvoiceType.PURCHASE);
        assertThat(actualList).isNotNull();
        assertThat(actualList).hasSize(2);
    }

    @Test
    void test_listSalesInvoices() {
        List<InvoiceDto> actualList = invoiceService.listInvoices(InvoiceType.SALES);
        assertThat(actualList).isNotNull();
        assertThat(actualList).hasSize(3);
        actualList.forEach(invoice -> {
            assertThat(invoice.getPrice()).isNotNull();
            assertThat(invoice.getTotal()).isNotNull();
            assertThat(invoice.getTax()).isNotNull();
        });
        List<String> list= actualList.stream().map(InvoiceDto::getInvoiceNo).toList();
        List<String> sortedList = actualList.stream().map(InvoiceDto::getInvoiceNo).sorted(Comparator.reverseOrder()).toList();
        assertThat(list).isEqualTo(sortedList);
    }

    @Test
    void test_generateNewInvoice() {
        InvoiceDto result = invoiceService.generateNewInvoiceDto(InvoiceType.SALES);
        assertThat(result).isNotNull();
        assertThat(result.getInvoiceNo()).isEqualTo("S-004");
    }

    @Test
    void test_save() {
        InvoiceDto invoiceDto = TestDocInitializer.getInvoiceDto(InvoiceStatus.AWAITING_APPROVAL, InvoiceType.PURCHASE);
        InvoiceDto actual = invoiceService.save(invoiceDto, InvoiceType.PURCHASE);
        assertThat(actual).isNotNull();
        assertThat(actual.getInvoiceNo()).isEqualTo(invoiceDto.getInvoiceNo());
    }

    @Test
    @Transactional(readOnly = true)
    void test_update() {
        ClientVendorDto dtoCV = TestDocInitializer.getClientVendorDto(ClientVendorType.CLIENT);
        InvoiceDto invoiceDto = invoiceService.findById(1L);
        invoiceDto.setClientVendor(dtoCV);

        InvoiceDto actual = invoiceService.updateInvoice(invoiceDto);

        assertThat(actual.getClientVendor().getClientVendorName()).isEqualTo(dtoCV.getClientVendorName());
    }

    @Test
    @Transactional(readOnly = true)
    void test_delete() {
        invoiceService.deleteInvoice(1L);
        Invoice invoice = invoiceRepository.findById(1L).orElseThrow(NoSuchElementException::new);
        assertTrue(invoice.getIsDeleted());
    }

    @Test
    void test_print() {
        InvoiceDto result = invoiceService.printInvoiceId(1L);
        assertThat(result.getPrice()).isNotNull();
        assertThat(result.getTax()).isNotNull();
        assertThat(result.getTotal()).isNotNull();
    }

    @Test
    void test_approveSalesInvoice() {
        InvoiceDto result = invoiceService.approveInvoice(1L);
        assertThat(result.getInvoiceStatus()).isEqualTo(InvoiceStatus.APPROVED);
        assertThat(result.getDate()).isEqualTo(LocalDate.now());
    }

    @Test
    @Transactional(readOnly = true)
    void test_approveSalesInvoice_throwExceptionForNotSufficientStock() {
        ProductDto productDto= productService.findById(1L);
        productDto.setQuantityInStock(1);
        productService.saveProduct(productDto);

        Throwable throwable = catchThrowable(() -> invoiceService.approveInvoice(1L));
        assertInstanceOf(ProductNotFoundException.class, throwable);


//        InvoiceDto invoiceDto = invoiceService.findById(4L);

//        assertThat(throwable).isInstanceOf(ProductNotFoundException.class);
    }

    @Test
    void test_approvePurchaseInvoice() {
        InvoiceDto result = invoiceService.approveInvoice(13L);
        assertThat(result.getInvoiceStatus()).isEqualTo(InvoiceStatus.APPROVED);
        assertThat(result.getDate()).isEqualTo(LocalDate.now());
    }

    @Test
    void test_listLastThreeApprovedInvoices() {
        List<InvoiceDto> dtoList = invoiceService.listLast3ApprovedInvoices();
        assertThat(dtoList).isNotNull();
        assertThat(dtoList).hasSize(2);
//        assertThat(dtoList).hasSize(3);
    }

    @Test
    void test_sumTotal() {
        BigDecimal total = invoiceService.sumTotal(InvoiceType.SALES);
        assertThat(total).isNotNull();
        assertThat(total).isEqualTo(new BigDecimal("660.000"));
    }

    @Test
    void test_sumProfitLoss() {
        BigDecimal result = invoiceService.sumProfitLoss();
        assertThat(result).isNotNull();
        assertThat(result).isEqualTo(new BigDecimal("110.00"));
    }
}
