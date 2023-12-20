package com.paymybuddy.controller;

import com.paymybuddy.model.UserAccount;
import com.paymybuddy.service.UserAccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import java.security.Principal;

@Controller
public class ProfileController {

    private final UserAccountService userAccountService;

    @Autowired
    public ProfileController(UserAccountService userAccountService) {
        this.userAccountService = userAccountService;
    }

    @GetMapping("/profile")
    public String getProfile(Principal principal) {
        UserAccount connectedUser = userAccountService.getUserWithEmail(principal.getName()).orElse(null);

        return "profile";
    }
}
