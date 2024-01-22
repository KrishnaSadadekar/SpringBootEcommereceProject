package com.example.shopmax.controllers;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
@Controller
public class DefaultController
{

    @GetMapping("/dashboard")
    public String redirectBasedOnRole(Authentication authentication) {
        if (authentication.getAuthorities().stream().anyMatch(r -> r.getAuthority().equals("Role_ADMIN"))) {
            return "redirect:/admin/dashboard";
        } else {
            return "redirect:/home";
        }
    }
}
