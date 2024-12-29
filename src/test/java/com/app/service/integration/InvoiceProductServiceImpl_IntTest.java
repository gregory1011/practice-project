package com.app.service.integration;


import com.app.dto.InvoiceDto;
import com.app.dto.InvoiceProductDto;
import com.app.dto.ProductDto;
import com.app.entity.InvoiceProduct;
import com.app.enums.InvoiceType;
import com.app.exceptions.InvoiceProductNotFoundException;
import com.app.exceptions.ProductLowLimitAlertException;
import com.app.exceptions.ProductNotFoundException;
import com.app.repository.InvoiceProductRepository;
import com.app.service.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
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
    void listAllApprovedInvoiceProductsOfCompany() {
        //when
        List<InvoiceProductDto> result = invoiceProductService.listAllApprovedInvoiceProductsOfCompany();
        //then
        assertThat(result).hasSize(3);
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
    @Transactional(readOnly = true) // for delete method to run DB with clause where isDeleted=true
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
        //given
        ProductDto productDto = productService.findById(1L);
        Integer initialQuantity = productDto.getQuantityInStock();
        InvoiceDto invoiceDto = new InvoiceDto();
        InvoiceDto saveInvoice = invoiceService.save(invoiceDto, InvoiceType.PURCHASE);

        InvoiceProductDto invoiceProductDto = new InvoiceProductDto();
        invoiceProductDto.setProduct(productDto);
        invoiceProductDto.setInvoice(saveInvoice);
        invoiceProductDto.setQuantity(10);
        invoiceProductService.save(saveInvoice.getId(), invoiceProductDto);

        //when
        invoiceProductService.updateQuantityInStockPurchase(saveInvoice.getId());
        //then
        productDto= productService.findById(ID);
        assertThat(productDto.getQuantityInStock()).isEqualTo(initialQuantity+10);
    }

    @Test
    void updateQuantityInStockSale_whenApprovedAndStockEnough() {
        //given
        ProductDto productDto= productService.findById(1L);
        Integer initialQuantityInStock = productDto.getQuantityInStock();
        //when
        invoiceProductService.updateQuantityInStockSale(4L);
        //then
        productDto= productService.findById(1L);
        assertThat(productDto.getQuantityInStock()).isEqualTo(initialQuantityInStock - 2);
    }

    @Test
    void updateQuantityInStockSale_shouldThrowException_stockNotEnough() {
        //given
        ProductDto productDto = productService.findById(1L);
        productDto.setQuantityInStock(1);
        productService.saveProduct(productDto);
        //when
        Throwable throwable = catchThrowable(() -> invoiceProductService.updateQuantityInStockSale(4L));
        //then
        assertThat(throwable).isInstanceOf(ProductNotFoundException.class);
    }

    @Test
    @WithMockUser(username = "manager@greentech.com", password= "Abc1", roles= "Manager")
    void calculateProfitOrLoss_withSuccess() {
        //when
        invoiceProductService.calculateProfitOrLoss(4L);
        //then
        List<InvoiceProductDto> result = invoiceProductService.listAllByInvoiceIdAndCalculateTotalPrice(4L);
        assertThat(result).isNotEmpty();
        result.forEach(ip-> assertThat(ip.getProfitLoss()).isNotEqualTo(BigDecimal.ZERO));
    }

    @Test
    void shouldInvokeLowQuantityAlert() {
        //given
        ProductDto productDto = productService.findById(1L);
        productDto.setQuantityInStock(1);
        productDto.setLowLimitAlert(5);
        productService.saveProduct(productDto);
        //when
        Throwable throwable = catchThrowable(() -> invoiceProductService.checkForLowQuantityAlert(4L));
        //then
        assertThat(throwable).isInstanceOf(ProductLowLimitAlertException.class);
    }

    @Test
    void shouldNotInvokeLowLimitAlert() {
        //given
        ProductDto productDto = productService.findById(1L);
        productDto.setLowLimitAlert(5);
        productDto.setQuantityInStock(6);
        productService.saveProduct(productDto);
        //when
        Throwable throwable = catchThrowable(() -> invoiceProductService.checkForLowQuantityAlert(4L));
        //then
        assertThat(throwable).isNull();
    }

}
