package com.app.controller;

import com.app.dto.CategoryDto;
import com.app.entity.Category;
import com.app.service.CategoryService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;


@Controller
@RequestMapping("/categories")
@AllArgsConstructor
public class CategoryController {


    private final CategoryService categoryService;

    @GetMapping("/list")
    public String listAllCategory(Model model) {
        model.addAttribute("categories", categoryService.listAllCategories());
        return "category/category-list";
    }

    @GetMapping("/create")
    public String createCategory(Model model) {
        model.addAttribute("newCategory", new Category());
        return "category/category-create";
    }

    @PostMapping("/create")
    public String createCategory(@Valid @ModelAttribute("newCategory") CategoryDto newCategory, BindingResult bindingResult,Model model) {
        boolean descriptionExists1 = categoryService.isDescriptionExists(newCategory);
        if (descriptionExists1) bindingResult.rejectValue("description", "err.description","Description already exists");
        if (bindingResult.hasErrors()) {
            return "category/category-create";
        }
        categoryService.saveCategory(newCategory);
        return "redirect:/categories/list";
    }

    @GetMapping("/update/{id}")
    public String updateCategory( @PathVariable("id") Long id, Model model) {
        model.addAttribute("category", categoryService.getCategoryById(id));
        return "category/category-update";
    }

    @PostMapping("/update/{id}")
    public String updateCategory(@Valid @PathVariable("id") Long id, @ModelAttribute("category")CategoryDto category, BindingResult bindingResult, Model model) {
        boolean descriptionExists = categoryService.isDescriptionExists(category);
        if (descriptionExists) bindingResult.rejectValue("description", "err.description","Description already exists");
        if (bindingResult.hasErrors()) {
           return "category/category-update";
       }
       categoryService.updateCategory(id, category);
       return "redirect:/categories/list";
    }

    @GetMapping("/delete/{id}")
    public String deleteCategory(@PathVariable("id") Long id) {
        categoryService.deleteById(id);
        return "redirect:/categories/list";
    }

}
