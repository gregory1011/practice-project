package com.app.controller;


import com.app.dto.PaymentDto;
import com.app.dto.common.ChargeRequest;
import com.app.service.PaymentService;
import com.app.service.SecurityService;
import com.stripe.exception.StripeException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;


@Controller
@RequestMapping("/payments")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;
    private final SecurityService securityService;


    @Value("${api.stripe.publicKey}")
    private String getStripePublicKey;
    private static final ChargeRequest.Currency CURRENCY = ChargeRequest.Currency.USD;


    // list payments by year, use @RequestParam 'year' from UI, then display the payments
    @GetMapping("/list")
    public String paymentList(@RequestParam(value = "year", required = false) Integer year, Model model) {
        int selectedYear = (year == null) ? LocalDate.now().getYear() : year;
        model.addAttribute("payments", paymentService.listAllPaymentsReportByYear(selectedYear));
        model.addAttribute("year", selectedYear);
        return "payment/payment-list";
    }

    //if the payment isPaid=true print the invoice id= ? ->
    @GetMapping("/toInvoice/{id}")
    public String toInvoice(@PathVariable Long id, Model model) {
        model.addAttribute("company", securityService.getLoggedInUser().getCompany());
        model.addAttribute("payment", paymentService.getPaymentById(id));
        return "payment/payment-invoice-print";
    }

    // if the payment isPaid=false do this ->
    @GetMapping("/newpayment/{id}")
    public String createNewPayment(@PathVariable Long id, Model model) {
        model.addAttribute("payment", paymentService.getPaymentById(id));
        model.addAttribute("stripePublicKey", getStripePublicKey);
        model.addAttribute("monthId", id);
        model.addAttribute("currency", CURRENCY);
        return "payment/payment-method";
    }

    // after the payment is done
    @PostMapping("/charge/{id}")
    public String charge(@PathVariable Long id, ChargeRequest chargeRequest, Model model) throws StripeException {
        chargeRequest.setCurrency(CURRENCY);
        PaymentDto dto = paymentService.charge(chargeRequest, id);
        model.addAttribute("chargeId", dto.getCompanyStripeId());
        model.addAttribute("description", dto.getDescription());
        return "payment/payment-result";
    }

}
