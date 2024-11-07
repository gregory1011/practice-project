package com.app.controller;


import com.app.dto.UserDto;
import com.app.service.CompanyService;
import com.app.service.RoleService;
import com.app.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

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
        return "/user/user-list";
    }

    @GetMapping("/create")
    public String createUser( Model model) {
        model.addAttribute("newUser", new UserDto());
        model.addAttribute("userRoles", roleService.listAllRoles());
        model.addAttribute("companies", companyService.listAllCompanies());
        return "/user/user-create";
    }

    @PostMapping("/create")
    public String saveUser(@ModelAttribute("newUser") UserDto newUser, Model model) {
        model.addAttribute("userRoles", roleService.listAllRoles());
        model.addAttribute("companies", companyService.listAllCompanies());
        userService.saveUser(newUser);
        return "redirect:/users/list";
    }
}
