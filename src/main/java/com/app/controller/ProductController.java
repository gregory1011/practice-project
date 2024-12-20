package com.app.controller;


import com.app.dto.ProductDto;
import com.app.entity.Product;
import com.app.enums.ProductUnit;
import com.app.service.CategoryService;
import com.app.service.ProductService;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Arrays;


@Controller
@RequestMapping ("/products")
@RequiredArgsConstructor
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
        boolean isNameExists= productService.isProductNameExists(newProduct);
        if (isNameExists) bindingResult.rejectValue("name","err.name", "Product name already exists");
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
        model.addAttribute("product", productService.findById(id));
        model.addAttribute("categories", categoryService.listAllCategories());
        model.addAttribute("productUnits", Arrays.stream(ProductUnit.values()).toList());
        return "product/product-update";
    }

    @PostMapping("/update/{id}")
    public String updateProduct(@Valid @PathVariable Long id, @ModelAttribute("product")ProductDto product, BindingResult bindingResult, Model model) {
        boolean productNameExists = productService.isProductNameExists(product);
        if (productNameExists) bindingResult.rejectValue("name","err.name", "Product name already exists");
        if (bindingResult.hasErrors()) {
            model.addAttribute("categories", categoryService.listAllCategories());
            model.addAttribute("productUnits", Arrays.stream(ProductUnit.values()).toList());
            return "product/product-update";
        }
        productService.updateProduct(product);
        return "redirect:/products/list";
    }

    @ModelAttribute
    private void commonAttributes(Model model) {
//        model.addAttribute("categories", categoryService.listAllCategories());
//        model.addAttribute("productUnits", Arrays.asList(ProductUnit.values()));
        model.addAttribute("title", "Accounting-App Products");
    }
}
