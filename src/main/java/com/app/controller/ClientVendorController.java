package com.app.controller;


import com.app.service.ClientVendorService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/clientVendor")
@AllArgsConstructor
public class ClientVendorController {


    private ClientVendorService clientVendorService;

    @GetMapping("/list")
    public String listClientVendor(Model model) {


        return "clientVendor/clientVendor-list";
    }


}
