package com.codeup.capstone3dprinting.controllers;


import com.codeup.capstone3dprinting.services.EmailService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;


@Controller
public class AuthenticationController {

    private final EmailService emailService;

    @GetMapping("/login")
    public String showLoginForm() {
        return "users/login";
    }

    public AuthenticationController(EmailService emailService) {
        this.emailService = emailService;
    }
}

