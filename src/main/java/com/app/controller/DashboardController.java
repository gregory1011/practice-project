package com.app.controller;

import com.app.service.DashboardService;
import com.app.service.InvoiceService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;


@Controller
@AllArgsConstructor
public class DashboardController {

    private final DashboardService dashboardService;
    private final InvoiceService invoiceService;


    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        model.addAttribute("summaryNumbers", dashboardService.getSummaryNumbers());
        model.addAttribute("invoices", invoiceService.listLast3ApprovedInvoices());
        model.addAttribute("exchangeRates", dashboardService.getCachedCurrencyDto());
        return "dashboard";
    }

}
