package com.app.controller;

import com.app.service.DashboardService;
import com.app.service.InvoiceProductService;
import com.app.service.InvoiceService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller
@RequestMapping("/dashboard")
@AllArgsConstructor
public class DashboardController {

    private final DashboardService dashboardService;
    private final InvoiceProductService invoiceProductService;
    private final InvoiceService invoiceService;



    @GetMapping()
    public String dashboard() {


        return "dashboard";
    }

}
