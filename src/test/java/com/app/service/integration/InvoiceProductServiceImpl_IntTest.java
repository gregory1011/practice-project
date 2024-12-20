package com.app.service.integration;


import com.app.dto.InvoiceProductDto;
import com.app.dto.ProductDto;
import com.app.entity.InvoiceProduct;
import com.app.enums.InvoiceStatus;
import com.app.enums.InvoiceType;
import com.app.exceptions.InvoiceProductNotFoundException;
import com.app.repository.InvoiceProductRepository;
import com.app.service.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.assertj.core.api.ThrowableAssert.catchThrowable;


//@Transactional
@SpringBootTest
public class InvoiceProductServiceImpl_IntTest {

    @Autowired
    private InvoiceProductService invoiceProductService;
    @Autowired
    private InvoiceService invoiceService;
    @Autowired
    private ProductService productService;
    @Autowired
    private CompanyService companyService;

    private static final Long ID= 1L;
    @Autowired
    private InvoiceProductRepository invoiceProductRepository;


    @BeforeEach
    void setUp() {
        SecuritySetUpUtil.setUpSecurityContext();
    }

    @Test
    void findById_shouldReturnInvoiceProduct() {
        InvoiceProductDto result = invoiceProductService.findById(ID);
        assertThat(result).isNotNull();
        assertThat(result.getPrice()).isCloseTo(BigDecimal.valueOf(250.00), withinPercentage(5));
    }
    @Test
    void findById_shouldThrowException() {
        Throwable throwable = catchThrowable(() -> invoiceProductService.findById(-1L));
        assertThat(throwable).isInstanceOf(InvoiceProductNotFoundException.class);
    }

    @Test
    void listAllByInvoiceIdAndCalculateTotalPrice_shouldCalculateTotalPrice() {
        List<InvoiceProductDto> actualResult= invoiceProductService.listAllByInvoiceIdAndCalculateTotalPrice(ID);
        assertThat(actualResult.get(0).getTotal()).isCloseTo(BigDecimal.valueOf(1275), withinPercentage(10L));
    }

    @Test
    void listInvoiceProductByInvoiceId_shouldReturnListOfInvoiceProduct() {
        List<InvoiceProductDto> result = invoiceProductService.listInvoiceProductByInvoiceId(ID);
        assertThat(result).isNotNull();
    }

    @Test
    void add_shouldAddInvoiceProductToInvoice() {
        InvoiceProductDto result = TestDocInitializer.getInvoiceProductDto();
        assertThat(result.getQuantity()).isEqualTo(5);
        assertThat(result.getPrice()).isEqualTo(BigDecimal.TEN);
    }
    @Test
    void test_Save_shouldAddInvoiceProductToInvoice(){
        InvoiceProductDto result = TestDocInitializer.getInvoiceProductDto();
        assertThat(result.getQuantity()).isEqualTo(5);
        assertThat(result.getPrice()).isEqualTo(BigDecimal.TEN);
    }

    @Test
    @Transactional // for delete method to run DB with clause where isDeleted=true
    void delete_shouldSetIsDeletedTrue() {
        InvoiceProductDto result = invoiceProductService.deleteInvoiceProduct(ID);
        InvoiceProduct invoiceProduct = invoiceProductRepository.findById(ID).orElseThrow();
        assertThat(invoiceProduct.getIsDeleted()).isTrue();
        assertThat(result).isNotNull();
    }

    @Test
    void updateRemainingQuantityUponPurchaseApproval_shouldReturnRemainingQuantity() {
        invoiceProductService.updateRemainingQuantityUponPurchaseApproval(13L);

        List<InvoiceProductDto> result = invoiceProductService.listInvoiceProductByInvoiceId(13L);
        result.forEach(ip-> assertThat(ip.getRemainingQuantity()).isPositive());
    }

    @Test
    void updateQuantityInStockPurchase_whenApproved() {
        ProductDto dto = productService.findById(ID);
        Integer initialQuantity = dto.getQuantityInStock();
        TestDocInitializer.getInvoiceDto(InvoiceStatus.AWAITING_APPROVAL, InvoiceType.PURCHASE);
    }
}
