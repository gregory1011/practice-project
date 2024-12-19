package com.app.service.unit;


import com.app.dto.CompanyDto;
import com.app.dto.InvoiceDto;
import com.app.dto.InvoiceProductDto;
import com.app.dto.ProductDto;
import com.app.entity.Invoice;
import com.app.entity.InvoiceProduct;
import com.app.entity.Product;
import com.app.enums.CompanyStatus;
import com.app.enums.InvoiceStatus;
import com.app.enums.InvoiceType;
import com.app.exceptions.InvoiceProductNotFoundException;
import com.app.exceptions.ProductLowLimitAlertException;
import com.app.exceptions.ProductNotFoundException;
import com.app.repository.InvoiceProductRepository;
import com.app.service.CompanyService;
import com.app.service.InvoiceService;
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
    private InvoiceService invoiceService;
    @Mock
    private ProductService productService; // this is being used when interacted with
    @Mock
    private CompanyService companyService;
    @Spy
    private MapperUtil mapperUtil= new MapperUtil(new ModelMapper());
    @InjectMocks
    private InvoiceProductServiceImpl invoiceProductService;

    private InvoiceProduct invoiceProduct;
    private InvoiceProductDto invoiceProductDto;
    private InvoiceDto invoiceDto;
    private Invoice invoice;
    private ProductDto productDto;
    private Product product;
    @BeforeEach
    void setUp() {
        invoiceProductDto= TestDocInitializer.getInvoiceProductDto();
        invoiceProduct= mapperUtil.convert(invoiceProductDto, new InvoiceProduct());
        invoiceDto= TestDocInitializer.getInvoiceDto(InvoiceStatus.APPROVED, InvoiceType.PURCHASE);
        invoice= mapperUtil.convert(invoiceDto, new Invoice());
        productDto= TestDocInitializer.getProductDto();
        product= mapperUtil.convert(productDto, new Product());
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

    @Test
    void shouldAddInvoiceProductToInvoice() {
        // given
        invoiceProduct.setInvoice(invoice);
        // when
        when(invoiceService.findById(anyLong())).thenReturn(invoiceDto);
        when(invoiceProductRepository.save(any())).thenReturn(invoiceProduct);

        InvoiceProductDto result = invoiceProductService.add(invoiceProductDto, 1L);

        // assert
        assertThat(result).usingRecursiveComparison()
                .withStrictTypeChecking()
                .ignoringExpectedNullFields()
                .ignoringFields("invoice.price", "invoice.tax")
                .isEqualTo(invoiceProductDto);
    }

    @Test
    void shouldGetAllApprovedInvoiceProductsOfCompany() {
        //given
        CompanyDto companyDto = TestDocInitializer.getCompany(CompanyStatus.ACTIVE);
        invoiceProduct.setInvoice(invoice);
        List<InvoiceProduct> list = List.of(invoiceProduct);
        //when
        when(companyService.getCompanyByLoggedInUser()).thenReturn(companyDto);
        when(invoiceProductRepository.retrieveByInvoiceCompanyIdAndAndInvoiceStatus(anyLong(), eq(InvoiceStatus.APPROVED))).thenReturn(list);

        List<InvoiceProductDto> result = invoiceProductService.listAllApprovedInvoiceProductsOfCompany();

        // assert
        assertNotNull(list);
        assertNotNull(result);
        assertThat(result.get(0).getInvoice().getInvoiceStatus())
                .usingRecursiveComparison()
                .isEqualTo(list.get(0).getInvoice().getInvoiceStatus());
    }

    @Test
    void saveShouldAddInvoiceProductToInvoiceList() {
        //given
        invoiceProduct.setInvoice(invoice);
        //when
        when(invoiceService.findById(anyLong())).thenReturn(invoiceDto);
        when(invoiceProductRepository.save(any())).thenReturn(invoiceProduct);

        InvoiceProductDto result = invoiceProductService.saveInvoiceProduct(1L, invoiceProductDto);
        // assert
        assertThat(result).usingRecursiveComparison()
                .withStrictTypeChecking()
                .ignoringExpectedNullFields()
                .ignoringFields("invoice.price", "invoice.tax")
                .isEqualTo(invoiceProductDto);
    }

    @Test
    void should_deleteInvoiceProductFromInvoiceList() {
        //given
        invoiceProduct.setIsDeleted(false);
        //when
        when(invoiceProductRepository.findById(anyLong())).thenReturn(Optional.of(invoiceProduct));
        when(invoiceProductRepository.save(any())).thenReturn(invoiceProduct);

        InvoiceProductDto result = invoiceProductService.deleteInvoiceProduct(anyLong());
        //then
        assertNotNull(result);
        assertTrue(invoiceProduct.getIsDeleted());
    }

    @Test
    void shouldUpdateRemainingQuantityOfInvoiceProductUponPurchaseApproval() {
        //when
        when(invoiceProductRepository.findAllByInvoiceId(anyLong())).thenReturn(List.of(invoiceProduct));
        when(invoiceProductRepository.save(any())).thenReturn(invoiceProduct);
        invoiceProductService.updateRemainingQuantityUponPurchaseApproval(anyLong());
        //then
        assertThat(invoiceProduct.getRemainingQuantity()).isEqualTo(5);
    }

    @Test
    void shouldUpdateQuantityInStockWhenPurchasedInvoiceProduct() {
        //when
        when(invoiceProductRepository.listProductsByInvoiceId(anyLong())).thenReturn(List.of(product)); // product quantity is 10 from TestDoc
        when(invoiceProductRepository.sumQuantityOfProducts(anyLong(), anyLong())).thenReturn(39); // this is the value we assume we get after sum Query DB
        invoiceProductService.updateQuantityInStockPurchase(anyLong());
        //then
        assertThat(product.getQuantityInStock()).isEqualTo(49); //this is what we expect to be total: product=10 + query=39
    }

    @Test
    void shouldUpdateQuantityInStockWhenSalesInvoiceProduct() {
        //when
        when(invoiceProductRepository.listProductsByInvoiceId(anyLong())).thenReturn(List.of(product)); // product quantity is 10 from TestDoc
        when(invoiceProductRepository.sumQuantityOfProducts(anyLong(), anyLong())).thenReturn(8); // this is the value we assume we get after sum Query DB
        invoiceProductService.updateQuantityInStockSale(anyLong());
        //then
        assertThat(product.getQuantityInStock()).isEqualTo(2); // this is what we expect to be total: product=10 - query=8
    }
    @Test
    void shouldUpdateQuantityInStockSale_throwExceptionIfStockNotEnough() {
        //when
        when(invoiceProductRepository.listProductsByInvoiceId(anyLong())).thenReturn(List.of(product)); // product quantity is 10 from TestDoc
        when(invoiceProductRepository.sumQuantityOfProducts(anyLong(), anyLong())).thenReturn(14); // this is the value we assume we get after sum Query DB
        Throwable throwable = catchThrowable(() -> invoiceProductService.updateQuantityInStockSale(anyLong()));
        //then
        assertThat(throwable).isInstanceOf(ProductNotFoundException.class);
        assertEquals("Stock of " + product.getName() + " is not enough to approve this invoice. Please update the invoice.", throwable.getMessage());
    }

    @Test
    void shouldCalculateProfitOrLoss() {
        //given
        CompanyDto companyDto = TestDocInitializer.getCompany(CompanyStatus.ACTIVE);
        invoiceProduct.setProduct(product);
        //when
        when(invoiceProductRepository.findAllByInvoiceId(anyLong())).thenReturn(List.of(invoiceProduct));
        when(companyService.getCompanyByLoggedInUser()).thenReturn(companyDto);
        when(invoiceProductRepository.getApprovedPurchaseInvoiceProducts(anyLong(), anyLong())).thenReturn(List.of(invoiceProduct));
        when(invoiceProductRepository.save(any())).thenReturn(invoiceProduct);

        invoiceProductService.calculateProfitOrLoss(anyLong());
        //then
        assertThat(invoiceProduct.getProfitLoss()).isEqualTo(BigDecimal.valueOf(55.0));
    }

    @Test
    void shouldInvokeLowQuantityAlert() {
        //given
        productDto.setQuantityInStock(4); //quantity in stock is 4
        productDto.setLowLimitAlert(10); // quantity alert is 10 and below
        Product product1 = mapperUtil.convert(productDto, new Product());
        //when
        when(invoiceProductRepository.listProductsByInvoiceId(anyLong())).thenReturn(List.of(product1));
        Throwable throwable = catchThrowable(() -> invoiceProductService.checkForLowQuantityAlert(anyLong()));
        //then
//        assertThat(throwable).isNull();
        assertThat(throwable).isInstanceOf(ProductLowLimitAlertException.class);
        assertEquals("Stock of ["+product1.getName()+"] decreased below low limit.", throwable.getMessage());
    }

    @Test
    void shouldNotInvokeLowQuantityAlert() {
        //when
        when(invoiceProductRepository.listProductsByInvoiceId(anyLong())).thenReturn(List.of(product));
        Throwable throwable = catchThrowable(() -> invoiceProductService.checkForLowQuantityAlert(anyLong()));
        //then
        assertNull(throwable);
    }
}
