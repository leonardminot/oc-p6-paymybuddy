package com.paymybuddy.controller;

import com.paymybuddy.model.UserAccount;
import com.paymybuddy.service.UserAccountService;
import com.paymybuddy.service.UserRelationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

@Controller
@Slf4j
public class ConnectionController {

    private final UserAccountService userAccountService;
    private final UserRelationService userRelationService;

    @Autowired
    public ConnectionController(UserAccountService userAccountService, UserRelationService userRelationService) {
        this.userAccountService = userAccountService;
        this.userRelationService = userRelationService;
    }

    @GetMapping("/addConnection")
    public String addConnectionInterface(Principal principal, Model model) {
        UserAccount connectedUser = userAccountService.getUserWithEmail(principal.getName()).orElse(null);
        List<UserAccount> relations = userRelationService.getRelationsFor(connectedUser);

        List<UserAccount> availableUsers = userAccountService.getAllUsers().stream()
                .filter(user -> !user.equals(connectedUser))
                .filter(user -> !relations.contains(user))
                .sorted(Comparator.comparing(UserAccount::getUsername))
                .toList();

        model.addAttribute("connectedUser", connectedUser);
        model.addAttribute("relations", relations);
        model.addAttribute("availableUsers", availableUsers);

        return "add-connection";
    }

    @PostMapping("/addConnection")
    public String addConnection(
            Principal principal,
            @RequestParam(value = "selectedUser", defaultValue = "ERROR_USER") String newRelationEmail,
            RedirectAttributes ra
    ) {
        UserAccount connectedUser = userAccountService.getUserWithEmail(principal.getName()).orElse(null);
        if (newRelationEmail.equals("ERROR_USER")) {
            ra.addFlashAttribute("noUserSelected", true);
            return "redirect:/addConnection";
        }

        log.info("New user connection email: " + newRelationEmail);
        try {
            UserAccount newRelationUser = userAccountService.getUserWithEmail(newRelationEmail).orElse(null);
            userRelationService.createRelation(connectedUser, newRelationUser);

        } catch (Exception exception) {
            log.error(exception.getMessage());
        }

        return "redirect:/addConnection";
    }
}
