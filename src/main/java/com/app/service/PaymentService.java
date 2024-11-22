package com.app.service;

import com.app.dto.PaymentDto;
import com.app.dto.common.ChargeRequest;
import com.stripe.exception.*;

import java.util.List;

public interface PaymentService {

    PaymentDto getPaymentById(Long id);
    List<PaymentDto> listAllPaymentsReportByYear(int selectedYear);
    PaymentDto charge(ChargeRequest chargeRequest, Long paymentId) throws StripeException;
}
