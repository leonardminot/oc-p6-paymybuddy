package com.paymybuddy.controller;

import com.paymybuddy.dto.BankAccountCreationCommandDTO;
import com.paymybuddy.model.BankAccount;
import com.paymybuddy.model.UserAccount;
import com.paymybuddy.service.BankAccountService;
import com.paymybuddy.service.UserAccountService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.security.Principal;
import java.util.List;

@Controller
@Slf4j
public class ProfileController {

    private final UserAccountService userAccountService;
    private final BankAccountService bankAccountService;

    @Autowired
    public ProfileController(UserAccountService userAccountService, BankAccountService bankAccountService) {
        this.userAccountService = userAccountService;
        this.bankAccountService = bankAccountService;
    }

    @GetMapping("/profile")
    public String getProfile(Principal principal, Model model) {
        UserAccount connectedUser = userAccountService.getUserWithEmail(principal.getName()).orElse(null);
        List<BankAccount> currentBankAccounts = bankAccountService.getBankAccountsFor(connectedUser);

        model.addAttribute("bankAccounts", currentBankAccounts);

        return "profile";
    }

    @GetMapping("/addBankAccount")
    public String addBankAccount(Model model) {
        model.addAttribute("bankAccount", new BankAccount());
        return "add-bank-account";
    }

    @PostMapping("/addBankAccount")
    public String newBankAccount(
            @ModelAttribute("bankAccount") BankAccount bankAccount,
            Principal principal
    ) {
        UserAccount connectedUser = userAccountService.getUserWithEmail(principal.getName()).orElse(null);
        try {
            bankAccountService.create(new BankAccountCreationCommandDTO(connectedUser, bankAccount.getIban(), bankAccount.getCountry()));
        } catch (Exception error) {
            log.error(error.getMessage());
        }
        return "redirect:/profile";
    }
}
