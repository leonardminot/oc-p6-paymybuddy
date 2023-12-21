package com.paymybuddy.controller;

import com.paymybuddy.dto.BankAccountCreationCommandDTO;
import com.paymybuddy.dto.BankTransactionCommandDTO;
import com.paymybuddy.model.BalanceByCurrency;
import com.paymybuddy.model.BankAccount;
import com.paymybuddy.model.BankTransaction;
import com.paymybuddy.model.UserAccount;
import com.paymybuddy.service.BalanceByCurrencyService;
import com.paymybuddy.service.BankAccountService;
import com.paymybuddy.service.BankTransactionService;
import com.paymybuddy.service.UserAccountService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.util.List;
import java.util.UUID;

@Controller
@Slf4j
public class ProfileController {

    private final UserAccountService userAccountService;
    private final BankAccountService bankAccountService;
    private final BankTransactionService bankTransactionService;
    private final BalanceByCurrencyService balanceByCurrencyService;

    @Autowired
    public ProfileController(UserAccountService userAccountService, BankAccountService bankAccountService, BankTransactionService bankTransactionService, BalanceByCurrencyService balanceByCurrencyService) {
        this.userAccountService = userAccountService;
        this.bankAccountService = bankAccountService;
        this.bankTransactionService = bankTransactionService;
        this.balanceByCurrencyService = balanceByCurrencyService;
    }

    @GetMapping("/profile")
    public String getProfile(Principal principal, Model model) {
        UserAccount connectedUser = userAccountService.getUserWithEmail(principal.getName()).orElse(null);
        List<BankAccount> currentBankAccounts = bankAccountService.getBankAccountsFor(connectedUser);
        List<BankTransaction> bankTransactions = bankTransactionService.fetchTransactionsFor(connectedUser);
        List<BalanceByCurrency> balanceByCurrencies = balanceByCurrencyService.fetchBalanceByCurrencyFor(connectedUser);

        model.addAttribute("bankAccounts", currentBankAccounts);
        model.addAttribute("bankTransactionCommand", new BankTransactionCommandDTO());
        model.addAttribute("bankTransactions", bankTransactions);
        model.addAttribute("balanceByCurrencies", balanceByCurrencies);

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

    @PostMapping("/addMoney/{idBank}")
    public String addMoney(
            @PathVariable("idBank") final UUID id,
            @ModelAttribute BankTransactionCommandDTO bankTransactionCommandDTO,
            @RequestParam String action,
            RedirectAttributes ra) {
        BankAccount bankAccount = bankAccountService.getBankAccount(id).orElse(null);

        if (action.equals("addMoney")) {
            try {
                bankTransactionService.newTransaction(new BankTransactionCommandDTO(
                        bankAccount,
                        bankTransactionCommandDTO.getAmount(),
                        bankTransactionCommandDTO.getCurrency()
                ));
            } catch (Exception e) {
                log.error(e.getMessage());
            }
        }

        if (action.equals("withdraw")) {
            try {
                bankTransactionService.newTransaction(new BankTransactionCommandDTO(
                        bankAccount,
                        -bankTransactionCommandDTO.getAmount(),
                        bankTransactionCommandDTO.getCurrency()
                ));
            } catch (Exception e) {
                log.error(e.getMessage());
                ra.addFlashAttribute("notEnoughMoney", true);
            }
        }
        return "redirect:/profile";
    }
}
