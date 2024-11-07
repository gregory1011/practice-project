package com.app.controller;

import com.app.dto.InvoiceDto;
import com.app.enums.ClientVendorType;
import com.app.enums.InvoiceType;
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


@Controller
@RequestMapping("/purchaseInvoices")
@AllArgsConstructor
public class PurchasesInvoiceController {


    private final InvoiceProductService invoiceProductService;
    private final InvoiceService invoiceService;
    private final ClientVendorService clientVendorService;


    @GetMapping("/list")
    public String list(Model model) {

        model.addAttribute("invoices", invoiceProductService.listAllInvoiceProductsByInvoiceTypeAndLoggedInUserOrderByInvoiceNoDesc(InvoiceType.PURCHASE));
        return "/invoice/purchase-invoice-list";
    }

    @GetMapping("/create")
    public String invoiceCreate(InvoiceDto invoiceDto, Model model) {
        invoiceDto.setInvoiceNo("P-"+invoiceService.getLastInvoiceId(InvoiceType.PURCHASE));
        model.addAttribute("newPurchaseInvoice", invoiceDto);
        model.addAttribute("vendors", clientVendorService.listClientVendorsByClientVendorType(ClientVendorType.VENDOR));
        return "/invoice/purchase-invoice-create";
    }


    @PostMapping("/create")
    public String invoicePost(@ModelAttribute("invoiceDto") InvoiceDto invoiceDto, Model model) {
        model.addAttribute("vendors", clientVendorService.listClientVendorsByClientVendorType(ClientVendorType.VENDOR));
        invoiceService.savePurchaseInvoice(invoiceDto);
        return "redirect:/purchaseInvoices/create";
    }



}
