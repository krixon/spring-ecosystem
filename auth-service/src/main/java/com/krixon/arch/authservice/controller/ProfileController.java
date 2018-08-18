package com.krixon.arch.authservice.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.security.Principal;

@Controller
public class ProfileController
{
    @GetMapping("/me")
    public String get(Model model, Principal principal)
    {
        model.addAttribute("user", principal);

        return "me";
    }
}
