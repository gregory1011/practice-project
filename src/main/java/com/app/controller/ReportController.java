package com.app.controller;

import com.app.service.ReportService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller
@RequestMapping("/reports")
@AllArgsConstructor
public class ReportController {


    private final ReportService reportService;


    @GetMapping("/profitLossData")
    public String listProfitLossData(Model model) {
        model.addAttribute("monthlyProfitLossDataMap", reportService.listMonthlyProfitLossMap());
        return "report/profit-loss-report";
    }

    @GetMapping("/stockData")
    public String listStockData(Model model) {
        model.addAttribute("invoiceProducts", reportService.listAllInvoiceProductsOfCompany());
        return "report/stock-report";
    }

}
