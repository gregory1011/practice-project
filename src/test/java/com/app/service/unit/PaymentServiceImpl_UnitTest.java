package com.app.service.unit;


import com.app.dto.CompanyDto;
import com.app.dto.PaymentDto;
import com.app.dto.common.ChargeRequest;
import com.app.entity.Payment;
import com.app.enums.CompanyStatus;
import com.app.exceptions.PaymentNotFoundException;
import com.app.repository.PaymentRepository;
import com.app.service.CompanyService;
import com.app.service.TestDocInitializer;
import com.app.service.impl.PaymentServiceImpl;
import com.app.util.MapperUtil;
import com.stripe.exception.StripeException;
import com.stripe.model.Charge;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.test.util.ReflectionTestUtils;


import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.ThrowableAssert.catchThrowable;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
public class PaymentServiceImpl_UnitTest {

    @Mock private PaymentRepository paymentRepository;
    @Mock private CompanyService companyService;
    @Spy private MapperUtil mapperUtil= new MapperUtil(new ModelMapper());
    @InjectMocks
    private PaymentServiceImpl paymentService;


//    @Value("${api.stripe.secretKey}")
//    private String stripeSecretKey;
//
//    @Value("${api.stripe.publicKey}")
//    private String stripePublicKey;

    private Payment payment;
    private PaymentDto paymentDto;
    private CompanyDto companyDto;
    @BeforeEach
    void setUp() {
        paymentDto= TestDocInitializer.getPaymentDto();
        payment= mapperUtil.convert(paymentDto, new Payment());
        companyDto= TestDocInitializer.getCompany(CompanyStatus.ACTIVE);

        //set the stripSecretKey value
        ReflectionTestUtils.setField(paymentService, "secretKey", "sk_test_4eC39HqLyjWDarjtT1zdp7dc");
    }

    @Test
    void findById_shouldReturnPayment() {
        //when
        when(paymentRepository.findById(anyLong())).thenReturn(Optional.of(payment));
        PaymentDto result = paymentService.getPaymentById(anyLong());
        //then part
        assertThat(result).usingRecursiveComparison()
                .ignoringExpectedNullFields()
                .ignoringFields("description")
                .isEqualTo(paymentDto);
    }
    @Test
    void getById_shouldReturnNotFound() {
        //when
        when(paymentRepository.findById(anyLong())).thenReturn(Optional.empty());
        Throwable throwable = catchThrowable(() -> paymentService.getPaymentById(anyLong()));
       //then part
        assertThat(throwable).isInstanceOf(PaymentNotFoundException.class);
    }

    @Test
    void listAllPaymentsReportByYear_paymentsExists() {
        //given part
        List<Payment> paymentList = List.of(payment, payment, payment);
        //when part
        when(companyService.getCompanyByLoggedInUser()).thenReturn(companyDto);
        when(paymentRepository.findByYearAndCompany_Id(anyInt(), anyLong())).thenReturn(paymentList);

        List<PaymentDto> result = paymentService.listAllPaymentsReportByYear(2024);
        //assert
        assertThat(result.get(0)).usingRecursiveComparison()
                .ignoringFields("description")
                .isEqualTo(paymentDto);
        assertThat(result).hasSize(3);
        verify(paymentRepository, never()).save(any(Payment.class));
    }

    @Test
    void chargePayment_Success() throws StripeException {
        //given part
        ChargeRequest chargeRequest = new ChargeRequest();
        chargeRequest.setStripeToken("token");

        Charge charge = new Charge();
        charge.setId("ch_1J2Y3a4b5c6d7e8f9g0h");
        charge.setStatus("succeeded");
        charge.setDescription("My app accounting subscription fee for : 2024 1");

        //when part
        when(paymentRepository.findById(anyLong())).thenReturn(Optional.of(payment));
        when(paymentRepository.save(any(Payment.class))).thenReturn(payment);

        MockedStatic<Charge> mockStatic = mockStatic(Charge.class);

            mockStatic.when(()-> Charge.create(any(Map.class))).thenReturn(charge);
            PaymentDto result= paymentService.charge(chargeRequest, anyLong());

            assertThat(result).usingRecursiveComparison()
                    .ignoringExpectedNullFields()
                    .isEqualTo(paymentDto);
            assertThat(result.getDescription()).isEqualTo("My app accounting subscription fee for : 2024 1");

    }

    @Test
    void chargePayment_Failure() throws StripeException {
//given part
        ChargeRequest chargeRequest = new ChargeRequest();
        chargeRequest.setStripeToken("token");

        Charge charge = new Charge();
        charge.setId("ch_1J2Y3a4b5c6d7e8f9g0h");
        charge.setStatus("failed");
        //when part
        when(paymentRepository.findById(anyLong())).thenReturn(Optional.of(payment));
        Map<Object, Object> map = Map.of();

        //when part
        when(Charge.create(Map.of())).thenReturn(charge);

        //assert
        assertThatThrownBy(()-> paymentService.charge(chargeRequest, anyLong()))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Invalid parameters");
    }

}
