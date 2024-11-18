package com.app.controller;

import com.app.dto.UserDto;
import com.app.service.CompanyService;
import com.app.service.RoleService;
import com.app.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;


@Controller
@RequestMapping("/users")
@AllArgsConstructor
public class UserController {

    private final UserService userService;
    private final RoleService roleService;
    private final CompanyService companyService;

    @GetMapping("/list")
    public String listUsers(Model model) {
        model.addAttribute("users", userService.listAllUsers());
        return "user/user-list";
    }

    @GetMapping("/create")
    public String createUser( Model model) {
        model.addAttribute("newUser", new UserDto());
        model.addAttribute("userRoles", roleService.listAllRoles());
        model.addAttribute("companies", companyService.listAllCompanies());
        return "user/user-create";
    }

    @PostMapping("/create")
    public String saveUser(@Valid @ModelAttribute("newUser") UserDto newUser, BindingResult bindingResult, Model model) {
        boolean isUsernameExist = userService.isUsernameExists(newUser);
        if(isUsernameExist) bindingResult.rejectValue("username", "err.username","A user with this email already exists. Please try with different email.");
        if (bindingResult.hasErrors()) {
            model.addAttribute("userRoles", roleService.listAllRoles());
            model.addAttribute("companies", companyService.listAllCompanies());
            return "user/user-create";
        }
        userService.saveUser(newUser);
        return "redirect:/users/list";
    }

    @GetMapping("/update/{id}")
    public String updateUser(@PathVariable("id")Long id, Model model) {
        model.addAttribute("user", userService.listById(id));
        model.addAttribute("userRoles", roleService.listAllRoles());
        model.addAttribute("companies", companyService.listAllCompanies());
        return "user/user-update";
    }

    @PostMapping("/update/{id}")
    public String postUser(@Valid @PathVariable("id")Long id, @ModelAttribute("user")UserDto user, BindingResult bindingResult, Model model) {
        boolean isUsernameExists = userService.isUsernameExists(user);
        if(isUsernameExists) bindingResult.rejectValue("username", "err.username","A user with this email already exists. Please try with different email.");
        if (bindingResult.hasErrors()) {
            model.addAttribute("userRoles", roleService.listAllRoles());
            model.addAttribute("companies", companyService.listAllCompanies());
            return "user/user-update";
        }
        userService.updateUser(user);
        return "redirect:/users/list";
    }

    @GetMapping("/delete/{id}")
    public String deleteUser(@PathVariable("id") Long id) {
        userService.deleteUser(id);
        return "redirect:/users/list";
    }

}
