package com.app.controller;

import com.app.dto.InvoiceDto;
import com.app.dto.InvoiceProductDto;
import com.app.enums.ClientVendorType;
import com.app.enums.InvoiceType;
import com.app.service.ClientVendorService;
import com.app.service.InvoiceProductService;
import com.app.service.InvoiceService;
import com.app.service.ProductService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;


@AllArgsConstructor
@Controller
@RequestMapping("/salesInvoices")
public class SalesInvoiceController {

    private final InvoiceService invoiceService;
    private final ClientVendorService clientVendorService;
    private final ProductService productService;
    private final InvoiceProductService invoiceProductService;

    @GetMapping("/list")
    public String list(Model model) {
        model.addAttribute("invoices", invoiceService.listAllByInvoiceType(InvoiceType.SALES));
        return "/invoice/sales-invoice-list";
    }

    @GetMapping("/create")
    public String createSaleInvoice(Model model) {
        model.addAttribute("newSalesInvoice", invoiceService.generateNewInvoiceDto(InvoiceType.SALES));
        model.addAttribute("clients", clientVendorService.listAllByClientVendorTypeAndCompanyId(ClientVendorType.CLIENT));
        return "/invoice/sales-invoice-create";
    }

    @PostMapping("/create")
    public String postSaleInvoice(@Valid @ModelAttribute("newSalesInvoice") InvoiceDto invoiceDto, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("clients", clientVendorService.listAllByClientVendorTypeAndCompanyId(ClientVendorType.CLIENT));
            return "/invoice/sales-invoice-create";
        }
        InvoiceDto dto = invoiceService.saveInvoice(invoiceDto, InvoiceType.SALES);
        return "redirect:/salesInvoices/update/"+dto.getId();
    }

    @GetMapping("/update/{id}")
    public String invoiceUpdate(@PathVariable("id") Long id, Model model) {
        model.addAttribute("invoice", invoiceService.listInvoiceById(id));
        model.addAttribute("clients", clientVendorService.listAllByClientVendorTypeAndCompanyId(ClientVendorType.CLIENT));
        model.addAttribute("products", productService.listAllProducts());
        model.addAttribute("newInvoiceProduct", new InvoiceProductDto());
        model.addAttribute("invoiceProducts", invoiceProductService.listAllByInvoiceIdAndCalculateTotalPrice(id));
        return "invoice/sales-invoice-update";
    }

    @PostMapping("/update/{id}")
    public String updateInvoice(@PathVariable("id") Long id, @ModelAttribute InvoiceDto invoiceDto) {
        invoiceService.updateInvoice(invoiceDto);
        return "redirect:/salesInvoices/update/"+id;
    }

    @PostMapping("/addInvoiceProduct/{invoiceId}")
    public String addProduct(@PathVariable("invoiceId") Long invoiceId,@Valid @ModelAttribute("newInvoiceProduct") InvoiceProductDto dto, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("invoice", invoiceService.listInvoiceById(invoiceId));
            model.addAttribute("clients", clientVendorService.listAllByClientVendorTypeAndCompanyId(ClientVendorType.CLIENT));
            model.addAttribute("products", productService.listAllProducts());
            model.addAttribute("invoiceProducts", invoiceProductService.listAllByInvoiceIdAndCalculateTotalPrice(invoiceId));
            return "invoice/purchase-invoice-update";
        }
        invoiceProductService.saveInvoiceProduct(invoiceId, dto);
        return "redirect:/salesInvoices/update/"+invoiceId;
    }

    @GetMapping("/removeInvoiceProduct/{invoiceId}/{invoiceProductId}")
    public String removeInvoiceProduct(@PathVariable("invoiceId") Long invoiceId, @PathVariable("invoiceProductId") Long invoiceProductId) {
        invoiceProductService.deleteInvoiceProduct(invoiceProductId);
        return "redirect:/salesInvoices/update/" + invoiceId;
    }

    @GetMapping("/delete/{id}")
    public String deleteInvoice(@PathVariable("id") Long id) {
        invoiceService.deleteInvoice(id);
        return "redirect:/salesInvoices/list";
    }

    @GetMapping("/approve/{id}")
    public String approveInvoice(@PathVariable("id") Long id) {

        return "redirect:/salesInvoices/list";
    }

    @GetMapping("/print/{id}")
    public String printInvoice(@PathVariable("id") Long id, Model model) {

        return "invoice/invoice_print";
    }


}
