package com.paymybuddy.controller;

import com.paymybuddy.model.UserAccount;
import com.paymybuddy.service.UserAccountService;
import com.paymybuddy.service.UserRelationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.security.Principal;

@Controller
public class ContactController {
    private final UserAccountService userAccountService;
    private final UserRelationService userRelationService;

    @Autowired
    public ContactController(UserAccountService userAccountService, UserRelationService userRelationService) {
        this.userAccountService = userAccountService;
        this.userRelationService = userRelationService;
    }

    @GetMapping("/contact")
    public String getContacts(Principal principal, Model model) {
        UserAccount connectedUser = userAccountService.getUserWithEmail(principal.getName()).orElse(null);
        model.addAttribute("relations", userRelationService.getRelationsFor(connectedUser));
        return "contact";
    }
}
