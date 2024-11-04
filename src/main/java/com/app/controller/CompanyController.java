package com.app.controller;

import com.app.dto.CompanyDto;
import com.app.enums.CompanyStatus;
import com.app.service.CompanyService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Controller
@RequestMapping("/companies")
@AllArgsConstructor
public class CompanyController {

    private final CompanyService companyService;

    @GetMapping("/list")
    public String listCompany(Model model) {
        model.addAttribute("companies", companyService.listAllCompanies());
        return "/company/company-list";
    }

    @GetMapping("/create")
    public String createCompany(Model model) {
        model.addAttribute("newCompany", new CompanyDto());
        return "/company/company-create";
    }

    @PostMapping("/create")
    public String createCompany(@Valid @ModelAttribute("newCompany") CompanyDto newCompany, BindingResult bindingResult, Model model) {
//        List<String> titles = companyService.listAllCompanies().stream().map(CompanyDto::getTitle).toList();
//        if(titles.contains(newCompany.getTitle())) {
//                bindingResult.rejectValue("title", "company.title", "Duplicate title");
////            throw new RuntimeException("Title already exists");
//        }

        if (bindingResult.hasErrors()) {
            return "/company/company-create";
        }
        companyService.saveCompany(newCompany);
        return "redirect:/companies/list";
    }

    @PostMapping("/activate/{id}")
    public String activateCompany(@PathVariable("id") Long id) {
        companyService.activateCompany(id);
        return "redirect:/companies/list";
    }

    @PostMapping("/deactivate/{id}")
    public String deactivateCompany(@PathVariable("id") Long id) {
        companyService.deactivateCompany(id);
        return "redirect:/companies/list";
    }

    @GetMapping("/update/{id}")
    public String editCompany(@PathVariable("id") Long id, Model model) {
        model.addAttribute("company", companyService.getCompanyById(id));
        return "/company/company-update";
    }
    @PostMapping("/update/{id}")
    public String updateCompany(@Valid @PathVariable("id") Long id, @ModelAttribute("company") CompanyDto companyDto) {
        companyService.updateCompany(id, companyDto);
        return "redirect:/companies/list";
    }

}
