package ru.kata.spring.boot_security.demo.controllers;


import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.servlet.ModelAndView;
import ru.kata.spring.boot_security.demo.models.User;
import ru.kata.spring.boot_security.demo.services.RoleService;
import ru.kata.spring.boot_security.demo.services.UserService;

import javax.validation.Valid;
import java.security.Principal;

@Controller
@RequestMapping("/admin")
public class AdminController {
    private final UserService userService;
    private final RoleService roleService;

    public AdminController(UserService userService, RoleService roleService) {
        this.userService = userService;
        this.roleService = roleService;
    }

    @GetMapping
    public ModelAndView displayAllUser(Principal principal){
        ModelAndView modelAndView = new ModelAndView("admin/index");
        User user = userService.findByUsername(principal.getName());
        modelAndView.addObject("user", user);
        modelAndView.addObject("users",userService.getUsers());
        modelAndView.addObject("roles", roleService.getRoles());
        return modelAndView;
    }

    @PostMapping("/new")
    public ModelAndView create(@ModelAttribute("user") @Valid User user,
                         @RequestParam("role_id") Long role_id,
                         BindingResult bindingResult){
        ModelAndView modelAndView = new ModelAndView("admin/index");
        ModelAndView modelAndViewRedirect = new ModelAndView("redirect:/admin");
        if (bindingResult.hasErrors()){
            return modelAndView;
        }
        user.setRole(roleService.getRole(role_id));
        userService.addUser(user);
        return modelAndViewRedirect;
    }

    @PatchMapping("/edit")
    public ModelAndView update(@ModelAttribute("user") @Valid User user,
                         @RequestParam("role_id") Long role_id,
                         BindingResult bindingResult){
        ModelAndView modelAndView = new ModelAndView("admin/index");
        ModelAndView modelAndViewRedirect = new ModelAndView("redirect:/admin");
        if (bindingResult.hasErrors()){
            return modelAndView;
        }
        user.setRole(roleService.getRole(role_id));
        userService.updateUser(user);
        return modelAndViewRedirect;
    }

    @DeleteMapping("/delete")
    public ModelAndView delete (@RequestParam("id") Long id){
        ModelAndView modelAndViewRedirect = new ModelAndView("redirect:/admin");
        userService.deleteUser(id);
        return modelAndViewRedirect;
    }
}
