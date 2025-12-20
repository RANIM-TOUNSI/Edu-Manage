package com.gestion.backend.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @GetMapping("/")
    public String home() {
        return "index"; // Thymeleaf template name
    }

    @GetMapping("/admin")
    public String adminHome() {
        return "admin/home";
    }

    @GetMapping("/formateur")
    public String formateurHome() {
        return "formateur/home";
    }

    @GetMapping("/etudiant")
    public String etudiantHome() {
        return "etudiant/home";
    }
}
