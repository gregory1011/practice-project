package com.app.controller;


import com.app.dto.ClientVendorDto;
import com.app.enums.ClientVendorType;
import com.app.service.AddressService;
import com.app.service.ClientVendorService;
import com.app.service.InvoiceService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Arrays;
import java.util.List;

@Controller
@RequestMapping("/clientVendors")
@AllArgsConstructor
public class ClientVendorController {


    private final ClientVendorService clientVendorService;
    private final InvoiceService invoiceService;
    private final AddressService addressService;

    @GetMapping("/list")
    public String listClientVendors(Model model) {
        List<ClientVendorDto> clientVendorDtoList = clientVendorService.listAllClientVendors();
        for (ClientVendorDto dto : clientVendorDtoList) {
            boolean hasInvoice= !invoiceService.listAllInvoiceByClientVendorId(dto.getId()).isEmpty();
            dto.setHasInvoice(hasInvoice);
        }
        model.addAttribute("clientVendors", clientVendorDtoList);
        return "clientVendor/clientVendor-list";
    }

    @GetMapping("/create")
    public String createClientVendor(Model model) {
        model.addAttribute("newClientVendor", new ClientVendorDto());
        model.addAttribute("clientVendorTypes", Arrays.asList(ClientVendorType.values()));
        model.addAttribute("countries", addressService.listAllCountries());
        return "clientVendor/clientVendor-create";
    }

    @PostMapping("/create")
    public String postClientVendor(@Valid @ModelAttribute("newClientVendor") ClientVendorDto newClientVendor, BindingResult bindingResult, Model model) {
        boolean isClientVendorNameExist= clientVendorService.isClientVendorNameExists(newClientVendor.getClientVendorName());
        if (isClientVendorNameExist) bindingResult.rejectValue("clientVendorName", "", "Name already exists.");
        if (bindingResult.hasErrors()) {
            model.addAttribute("clientVendorTypes", Arrays.asList(ClientVendorType.values()));
            model.addAttribute("countries", addressService.listAllCountries());
            return "clientVendor/clientVendor-create";
        }
        clientVendorService.saveClientVendor(newClientVendor);
        return "redirect:/clientVendors/list";
    }

    @GetMapping("/delete/{id}")
    public String deleteClientVendors(@PathVariable("id") Long id) {
        clientVendorService.deleteClientVendor(id);
        return "redirect:/clientVendors/list";
    }

    @GetMapping("/update/{id}")
    public String updateClientVendor(@PathVariable("id") Long id, Model model) {
        model.addAttribute("clientVendor", clientVendorService.findById(id));
        model.addAttribute("clientVendorTypes", Arrays.asList(ClientVendorType.values()));
        model.addAttribute("countries", addressService.listAllCountries());
        return "clientVendor/clientVendor-update";
    }

    @PostMapping("/update/{id}")
    public String updClientVendor(@Valid @PathVariable("id")Long id, @ModelAttribute("clientVendor")ClientVendorDto clientVendor, BindingResult bindingResult, Model model){
        if (bindingResult.hasErrors()) {
            model.addAttribute("clientVendor", clientVendorService.findById(id));
            model.addAttribute("clientVendorTypes", Arrays.asList(ClientVendorType.values()));
            model.addAttribute("countries", addressService.listAllCountries());
            return "clientVendor/clientVendor-update";
        }
        clientVendor.setId(id);
        clientVendorService.updateClientVendor(clientVendor);
        return "redirect:/clientVendors/list";
    }

}
