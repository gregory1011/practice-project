package com.app.controller;

import com.app.dto.InvoiceDto;
import com.app.dto.InvoiceProductDto;
import com.app.enums.ClientVendorType;
import com.app.enums.InvoiceType;
import com.app.service.*;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;



@Controller
@RequestMapping("/purchaseInvoices")
@RequiredArgsConstructor
public class PurchasesInvoiceController {


    private final InvoiceService invoiceService;
    private final ClientVendorService clientVendorService;
    private final ProductService productService;
    private final InvoiceProductService invoiceProductService;
    private final SecurityService securityService;


    @GetMapping("/list")
    public String list(Model model) {
        model.addAttribute("invoices", invoiceService.listAllByInvoiceType(InvoiceType.PURCHASE));
        return "invoice/purchase-invoice-list";
    }

    @GetMapping("/create")
    public String invoiceCreate(Model model) {
        model.addAttribute("newPurchaseInvoice", invoiceService.generateNewInvoiceDto(InvoiceType.PURCHASE));
        model.addAttribute("vendors", clientVendorService.listAllByClientVendorType(ClientVendorType.VENDOR));
        return "invoice/purchase-invoice-create";
    }

    @PostMapping("/create")
    public String invoicePost(@Valid @ModelAttribute("newPurchaseInvoice") InvoiceDto invoiceDto, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("vendors", clientVendorService.listAllByClientVendorType(ClientVendorType.VENDOR));
            return "invoice/purchase-invoice-create";
        }
        InvoiceDto dto = invoiceService.saveInvoice(invoiceDto, InvoiceType.PURCHASE);
        return "redirect:/purchaseInvoices/update/"+dto.getId();
    }

    @GetMapping("/update/{id}")
    public String invoiceUpdate(@PathVariable("id") Long id, Model model) {
        model.addAttribute("invoice", invoiceService.listInvoiceById(id));
        model.addAttribute("vendors", clientVendorService.listAllByClientVendorType(ClientVendorType.VENDOR));
        model.addAttribute("products", productService.listAllProducts());
        model.addAttribute("newInvoiceProduct", new InvoiceProductDto());
        model.addAttribute("invoiceProducts", invoiceProductService.listAllByInvoiceIdAndCalculateTotalPrice(id));
        return "invoice/purchase-invoice-update";
    }

    @PostMapping("/update/{id}")
    public String updateInvoice(@PathVariable("id") Long id, @ModelAttribute() InvoiceDto invoiceDto) {
        invoiceService.updateInvoice(invoiceDto);
        return "redirect:/purchaseInvoices/update/"+id;
    }

    @PostMapping("/addInvoiceProduct/{invoiceId}")
    public String addProduct(@PathVariable("invoiceId") Long invoiceId,@Valid @ModelAttribute("newInvoiceProduct") InvoiceProductDto dto, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("invoice", invoiceService.listInvoiceById(invoiceId));
            model.addAttribute("vendors", clientVendorService.listAllByClientVendorType(ClientVendorType.VENDOR));
            model.addAttribute("products", productService.listAllProducts());
            model.addAttribute("invoiceProducts", invoiceProductService.listAllByInvoiceIdAndCalculateTotalPrice(invoiceId));
            return "invoice/purchase-invoice-update";
        }
        invoiceProductService.saveInvoiceProduct(invoiceId, dto);
        return "redirect:/purchaseInvoices/update/"+invoiceId;
    }

    @GetMapping("/removeInvoiceProduct/{invoiceId}/{invoiceProductId}")
    public String removeInvoiceProduct(@PathVariable("invoiceId") Long invoiceId, @PathVariable("invoiceProductId") Long invoiceProductId) {
        invoiceProductService.deleteInvoiceProduct(invoiceProductId);
        return "redirect:/purchaseInvoices/update/" + invoiceId;
    }

    @GetMapping("/delete/{id}")
    public String deleteInvoice(@PathVariable("id") Long id) {
        invoiceService.deleteInvoice(id);
        return "redirect:/purchaseInvoices/list";
    }

    @GetMapping("/approve/{id}")
    public String approveInvoice(@PathVariable("id") Long id) {
        invoiceService.approveInvoice(id);
        return "redirect:/purchaseInvoices/list";
    }

    @GetMapping("/print/{id}")
    public String printInvoice(@PathVariable("id") Long id, Model model) {
        model.addAttribute("invoice", invoiceService.printInvoiceId(id));
        model.addAttribute("company", securityService.getLoggedInUser().getCompany());
        model.addAttribute("invoiceProducts", invoiceProductService.listAllByInvoiceIdAndCalculateTotalPrice(id));
        return "invoice/invoice_print";
    }

    @ModelAttribute
    private void commonAttributes(Model model) {
//        model.addAttribute("vendors", clientVendorService.listByClientVendorType(ClientVendorType.VENDOR));
//        model.addAttribute("products", productService.listAllProducts());
        model.addAttribute("title", "Accounting-App Purchase Invoice");
    }

}
