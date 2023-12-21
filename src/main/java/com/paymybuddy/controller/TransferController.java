package com.paymybuddy.controller;

import com.paymybuddy.dto.UserTransactionCommand;
import com.paymybuddy.dto.UserTransactionDTO;
import com.paymybuddy.model.UserAccount;
import com.paymybuddy.service.UserAccountService;
import com.paymybuddy.service.UserRelationService;
import com.paymybuddy.service.UserTransactionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.util.List;

@Controller
@Slf4j
public class TransferController {

    private final UserAccountService userAccountService;
    private final UserRelationService userRelationService;
    private final UserTransactionService userTransactionService;

    @Autowired
    public TransferController(UserAccountService userAccountService, UserRelationService userRelationService, UserTransactionService userTransactionService) {
        this.userAccountService = userAccountService;
        this.userRelationService = userRelationService;
        this.userTransactionService = userTransactionService;
    }

    @GetMapping("/transfer")
    public String transferPage(Principal principal, Model model) {
        UserAccount connectedUser = userAccountService.getUserWithEmail(principal.getName()).orElse(null);
        List<UserAccount> relations = userRelationService.getRelationsFor(connectedUser);
        List<UserTransactionDTO> transactions = userTransactionService.getTransactionsFor(connectedUser);

        model.addAttribute("relations", relations);
        model.addAttribute("transferCommand", new UserTransactionCommand());
        model.addAttribute("transactions", transactions);
        model.addAttribute("connectedUser", connectedUser);

        return "transfer";
    }

    @PostMapping("/sendMoney")
    public String sendMoney(
            @ModelAttribute("transferCommand") UserTransactionCommand userTransactionCommand,
            Principal principal,
            RedirectAttributes ra
    ) {
        UserAccount connectedUser = userAccountService.getUserWithEmail(principal.getName()).orElse(null);
        try {
            userTransactionService.performTransaction(new UserTransactionCommand(
                    connectedUser,
                    userTransactionCommand.getToUser(),
                    userTransactionCommand.getDescription(),
                    userTransactionCommand.getCurrency(),
                    userTransactionCommand.getAmount()
            ));
        } catch (Exception e) {
            ra.addFlashAttribute("hasError", true);
            log.error(e.getMessage());
        }
        return "redirect:/transfer";
    }
}
