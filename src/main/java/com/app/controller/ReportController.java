package com.app.controller;


import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/reports")
@AllArgsConstructor
public class ReportController {


    @GetMapping("/profitLossData")
    public String listProfitLossData(Model model) {

        return "/report/profit-loss-report";
    }

    @GetMapping("/stockData")
    public String listStockData(Model model) {

        return "/report/stock-report";
    }
}
