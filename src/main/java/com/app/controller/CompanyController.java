package com.app.controller;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/companies")
@AllArgsConstructor
public class CompanyController {


    @GetMapping("/list")
    public String listCompany(Model model) {

        return "/company/company-list";
    }


}
