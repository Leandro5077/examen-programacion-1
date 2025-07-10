package com.example.crudweb.controller;

import com.example.crudweb.model.User;
import com.example.crudweb.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
public class AuthController {

    @Autowired
    private UserService userService;

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/register")
    public String registerForm(Model model) {
        model.addAttribute("user", new User());
        return "register";
    }

    @PostMapping("/register")
    public String registerUser(@Valid @ModelAttribute("user") User user, BindingResult result, Model model) {
        if (userService.findByUsername(user.getUsername()) != null) {
            result.rejectValue("username", null, "Este usuario ya existe");
        }

        if (result.hasErrors()) {
            return "register";
        }

        userService.save(user);
        return "redirect:/login";
    }

    @GetMapping("/")
    public String home() {
        return "home";
    }
}