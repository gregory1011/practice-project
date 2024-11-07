package com.app.controller;


import com.app.dto.ProductDto;
import com.app.entity.Product;
import com.app.enums.ProductUnit;
import com.app.service.CategoryService;
import com.app.service.ProductService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Arrays;

@Controller
@RequestMapping ("/products")
@AllArgsConstructor
public class ProductController {

    private final ProductService productService;
    private final CategoryService categoryService;

    @GetMapping("/list")
    public String listProducts(Model model) {
        model.addAttribute("products", productService.listAllProducts());
        return "product/product-list";
    }

    @GetMapping("/create")
    public String createProduct(Model model) {
        model.addAttribute("newProduct", new Product());
        model.addAttribute("categories", categoryService.listAllCategories());
        model.addAttribute("productUnits", Arrays.stream(ProductUnit.values()).toList());
        return "product/product-create";
    }

    @PostMapping("/create")
    public String createProduct(@Valid @ModelAttribute("newProduct") ProductDto newProduct, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("categories", categoryService.listAllCategories());
            model.addAttribute("productUnits", Arrays.stream(ProductUnit.values()).toList());
            return "product/product-create";
        }
        productService.saveProduct(newProduct);
        return "redirect:/products/list";
    }

    @GetMapping("/delete/{id}")
    public String deleteProduct(@PathVariable Long id) {
        productService.deleteProductById(id);
        return "redirect:/products/list";
    }

    @GetMapping("/update/{id}")
    public String updateProduct(@PathVariable Long id, Model model) {
        model.addAttribute("product", productService.listProductById(id));
        model.addAttribute("categories", categoryService.listAllCategories());
        model.addAttribute("productUnits", Arrays.stream(ProductUnit.values()).toList());
        return "product/product-update";
    }

    @PostMapping("/update/{id}")
    public String updateProduct(@Valid @PathVariable Long id, @ModelAttribute("product")ProductDto product, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("categories", categoryService.listAllCategories());
            model.addAttribute("productUnits", Arrays.stream(ProductUnit.values()).toList());
            return "product/product-update";
        }
        productService.updateProduct(product);
        return "redirect:/products/list";
    }

    }
