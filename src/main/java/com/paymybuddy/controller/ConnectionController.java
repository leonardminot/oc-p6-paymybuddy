package com.paymybuddy.controller;

import com.paymybuddy.model.UserAccount;
import com.paymybuddy.service.UserAccountService;
import com.paymybuddy.service.UserRelationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.security.Principal;
import java.util.List;

@Controller
public class ConnectionController {

    private final UserAccountService userAccountService;
    private final UserRelationService userRelationService;

    @Autowired
    public ConnectionController(UserAccountService userAccountService, UserRelationService userRelationService) {
        this.userAccountService = userAccountService;
        this.userRelationService = userRelationService;
    }

    @GetMapping("/addConnection")
    public String addConnection(Principal principal, Model model) {
        UserAccount connectedUser = userAccountService.getUserWithEmail(principal.getName()).orElse(null);
        List<UserAccount> relations = userRelationService.getRelationsFor(connectedUser);
        model.addAttribute("connectedUser", connectedUser);
        model.addAttribute("relations", relations);
        return "add-connection";
    }
}
