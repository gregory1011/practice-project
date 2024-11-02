package com.app.controller;


import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping ("/product")
@AllArgsConstructor
public class ProductController {


    @GetMapping("/list")
    public String listProducts(Model model) {

        return "product/product-list";
    }

}
