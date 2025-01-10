package com.app.service.integration;


import com.app.dto.CompanyDto;
import com.app.dto.PaymentDto;
import com.app.dto.common.ChargeRequest;
import com.app.entity.Payment;
import com.app.enums.CompanyStatus;
import com.app.exceptions.PaymentNotFoundException;
import com.app.repository.PaymentRepository;
import com.app.service.CompanyService;
import com.app.service.SecuritySetUpUtil;
import com.app.service.TestDocInitializer;
import com.app.service.impl.PaymentServiceImpl;
import com.app.util.MapperUtil;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.Charge;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.MockedStatic;
import org.mockito.Spy;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


import java.util.List;


@SpringBootTest
public class PaymentServiceImpl_IntTest {


    @Autowired private CompanyService companyService;
    @Autowired private PaymentRepository paymentRepository;
    @Autowired private PaymentServiceImpl paymentService;
    @Captor private ArgumentCaptor<Payment> paymentArgumentCaptor;
    @Spy private MapperUtil mapperUtil= new MapperUtil(new ModelMapper());

    private Payment payment;
    private PaymentDto paymentDto;
    private CompanyDto companyDto;

    @Value("${api.stripe.secretKey}")
    private String secretKey;

    @BeforeEach
    void setUp() {
        SecuritySetUpUtil.setUpSecurityContext();

        paymentDto= TestDocInitializer.getPaymentDto();
        payment= mapperUtil.convert(paymentDto, new Payment());
        companyDto= TestDocInitializer.getCompany(CompanyStatus.ACTIVE);

        //set the stripSecretKey value
//        ReflectionTestUtils.setField(paymentService, "secretKey", "sk_test_4eC39HqLyjWDarjtT1zdp7dc");
    }



    @Test
    void test_getPaymentById_Success() {
        PaymentDto result= paymentService.getPaymentById(1L);
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(1L);
    }

    @Test
    void test_getPaymentById_Failure() {
        Throwable throwable = catchThrowable(() -> paymentService.getPaymentById(-1L));
        assertThat(throwable).isNotNull();
        assertThat(throwable).isInstanceOf(PaymentNotFoundException.class);
    }

    @Test
    void test_listAllPaymentsReportByYear() {
        List<PaymentDto> result = paymentService.listAllPaymentsReportByYear(2024);
        assertThat(result).isNotNull();
        assertThat(result).isNotEmpty();
    }

//    @Transactional
    @Test
    void test_charge_success() throws StripeException {
        Stripe.apiKey= secretKey;
        when(paymentService.getPaymentById(1L)).thenReturn(paymentDto);

        ChargeRequest chargeRequest= new ChargeRequest();
        chargeRequest.setStripeToken("token");

        Charge charge = mock(Charge.class);
        when(charge.getStatus()).thenReturn("succeeded");
        when(charge.getId()).thenReturn("ch_123");
        when(charge.getDescription()).thenReturn("description");

//        MockedStatic<Charge> mockStatic = mock(Charge.class);
        when(Charge.create(anyMap())).thenReturn(charge);

        PaymentDto result = paymentService.charge(chargeRequest, 1L);
        assertNotNull(result);
        verify(paymentRepository).save(paymentArgumentCaptor.capture());

        Payment savedPayment = paymentArgumentCaptor.getValue();
        assertThat(savedPayment).isNotNull();
        assertTrue(savedPayment.isPaid());
        assertEquals("ch_123", savedPayment.getCompanyStripeId());

//        Charge charge = new Charge();
//        charge.setId("ch_1J2Y3a4b5c6d7e8f9g0h");
//        charge.setStatus("succeeded");
//        charge.setDescription("My app accounting subscription fee for : 2024 1");

//        PaymentDto result= paymentService.charge(chargeRequest, 1L);
//        assertThat(result).isNotNull();
    }


}
