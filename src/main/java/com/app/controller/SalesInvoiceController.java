package com.app.controller;

import com.app.enums.InvoiceType;
import com.app.repository.InvoiceProductRepository;
import com.app.service.InvoiceProductService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller
@RequestMapping("/salesInvoices")
public class SalesInvoiceController {

    private final InvoiceProductService invoiceProductService;

    public SalesInvoiceController(InvoiceProductService invoiceProductService) {
        this.invoiceProductService = invoiceProductService;
    }

    @GetMapping("/list")
    public String list(Model model) {

//        model.addAttribute("invoices", invoiceProductService.listAllInvoiceProductsByInvoiceType(InvoiceType.SALES));
        return "/invoice/sales-invoice-list";
    }

}
