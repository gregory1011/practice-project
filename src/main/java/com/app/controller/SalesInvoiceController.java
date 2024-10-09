package com.app.controller;

import com.app.dto.InvoiceDto;
import com.app.enums.ClientVendorType;
import com.app.enums.InvoiceType;
import com.app.repository.InvoiceProductRepository;
import com.app.service.ClientVendorService;
import com.app.service.InvoiceProductService;
import com.app.service.InvoiceService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;


@AllArgsConstructor
@Controller
@RequestMapping("/salesInvoices")
public class SalesInvoiceController {

    private final InvoiceProductService invoiceProductService;
    private final ClientVendorService clientVendorService;
    private final InvoiceService invoiceService;

    @GetMapping("/list")
    public String list(Model model) {
        model.addAttribute("invoices", invoiceProductService.listAllInvoiceProductsByInvoiceTypeAndLoggedInUserOrderByInvoiceNoDesc(InvoiceType.SALES));
        return "/invoice/sales-invoice-list";
    }

    @GetMapping("/create")
    public String createSaleInvoice(Model model) {

        model.addAttribute("newSalesInvoice", new InvoiceDto());
        model.addAttribute("clients", clientVendorService.listAllClientVendors());
        return "/invoice/sales-invoice-create";
    }

    @PostMapping("/create")
    public String postSaleInvoice(@ModelAttribute("invoiceDto") InvoiceDto invoiceDto, Model model) {
        model.addAttribute("newSalesInvoice", invoiceDto);
        invoiceService.saveSaleInvoice(invoiceDto);
        return "/invoice/sales-invoice-create";
    }

}
