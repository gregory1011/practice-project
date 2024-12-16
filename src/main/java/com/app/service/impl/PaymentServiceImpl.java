package com.app.service.impl;

import com.app.dto.CompanyDto;
import com.app.dto.PaymentDto;
import com.app.dto.common.ChargeRequest;
import com.app.entity.Company;
import com.app.entity.Payment;
import com.app.enums.Months;
import com.app.exceptions.PaymentNotFoundException;
import com.app.repository.PaymentRepository;
import com.app.service.PaymentService;
import com.app.service.SecurityService;
import com.app.util.MapperUtil;
import com.stripe.Stripe;
import com.stripe.exception.*;
import com.stripe.model.Charge;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Service
public class PaymentServiceImpl implements PaymentService {

    private final PaymentRepository paymentRepository;
    private final MapperUtil mapperUtil;
    private final SecurityService securityService;

    public PaymentServiceImpl(PaymentRepository paymentRepository, MapperUtil mapperUtil, SecurityService securityService) {
        this.paymentRepository = paymentRepository;
        this.mapperUtil = mapperUtil;
        this.securityService = securityService;
    }


    @Value("${api.stripe.secretKey}")
    private String secretKey;

    @Override
    public PaymentDto getPaymentById(Long id) {
        Payment payment = paymentRepository.findById(id).orElseThrow(PaymentNotFoundException::new);
        return mapperUtil.convert(payment, new PaymentDto());
    }

    @Override
    public List<PaymentDto> listAllPaymentsReportByYear(int selectedYear) {
        Long companyId = securityService.getLoggedInUser().getCompany().getId();
        List<Payment> list = paymentRepository.findByYearAndCompany_Id(selectedYear, companyId);
//        if (list.isEmpty()){ // this block of code is to upload new data in database for test purpose.
//            createPayments(selectedYear);
//        }
        return list.stream()
                .sorted(Comparator.comparing(Payment::getMonth))
                .map(each->mapperUtil.convert(each, new PaymentDto()))
                .toList();
    }

    @Override
    public PaymentDto charge(ChargeRequest chargeRequest, Long paymentId) throws StripeException {

        // get the payment from DB to modify once the payment is done.
        Payment payment = paymentRepository.findById(paymentId).orElseThrow(PaymentNotFoundException::new);

        // assigned secretKey directly to Stripe.
        Stripe.apiKey= secretKey;

        // write custom description
        String description= "Company accounting subscription fee for "+payment.getMonth()+ " "+payment.getYear()+" -> $"+payment.getAmount();
        chargeRequest.setDescription(description);

        // make map for the request header api object;
        Map<String, Object> chargeParams= new HashMap<>();
        chargeParams.put("amount", payment.getAmount().intValue());
        chargeParams.put("currency", chargeRequest.getCurrency());
        chargeParams.put("description", chargeRequest.getDescription());
        chargeParams.put("source", chargeRequest.getStripeToken());

        // get response charge object when submit payment:
        Charge stripeCharge = Charge.create(chargeParams);

        // verify if the payment was not succeeded
        if (!stripeCharge.getStatus().equals("succeeded")) throw new IllegalArgumentException("Charge failed. Please try again!");

        //make DB modification before saving
        payment.setPaid(true);
        payment.setCompanyStripeId(stripeCharge.getId());
        payment.setPaymentDate(LocalDate.now());
        paymentRepository.save(payment);

        // return dto object to UI.
        PaymentDto dto = mapperUtil.convert(payment, new PaymentDto());
        dto.setDescription(description);
        return dto;
    }

    private void createPayments(int year) {

        CompanyDto company = securityService.getLoggedInUser().getCompany();
//        String[] months = DateFormatSymbols.getInstance().getMonths();
        for(Months month : Months.values()){
            Payment payment = new Payment();
            payment.setYear(year);
            payment.setMonth(month);
            payment.setPaid(false);
            payment.setAmount(BigDecimal.valueOf(250));
            payment.setCompany(mapperUtil.convert(company, new Company()));
            paymentRepository.save(payment);
        }
    }



}
