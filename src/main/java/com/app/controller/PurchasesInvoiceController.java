package com.app.controller;

import com.app.service.InvoiceProductService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller
@RequestMapping("/purchasesInvoices")
public class PurchasesInvoiceController {


    private final InvoiceProductService invoiceProductService;

    public PurchasesInvoiceController( InvoiceProductService invoiceProductService) {
        this.invoiceProductService = invoiceProductService;
    }

    @GetMapping("/list")
    public String list(Model model) {

        model.addAttribute("invoices", invoiceProductService.listAllInvoiceProductsByInvoiceTypePurchase());
        return "/invoice/purchase-invoice-list";
    }

    @GetMapping("/create")
    public String create(Model model) {

        return "/invoice/purchase-invoice-create";
    }



}
