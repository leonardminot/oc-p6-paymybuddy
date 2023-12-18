package com.paymybuddy.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@Slf4j
public class UIController {

    @GetMapping("/login")
    public String login(@RequestParam(value = "hasError", required = false) boolean hasError, Model model) {
        model.addAttribute("hasError", hasError);
        log.info("HasError: " + hasError);
        return "login";
    }
    @GetMapping("/createAccount")
    public String createAccount() {
        return "createAccount";
    }

    @GetMapping("/")
    public String showFirstPage() {
        return "connexion";
    }
}
