package com.paymybuddy.controller;

import com.paymybuddy.Exception.BalanceAndTransferException;
import com.paymybuddy.Exception.EmptyFieldException;
import com.paymybuddy.dto.BankAccountCreationCommandDTO;
import com.paymybuddy.dto.BankTransactionCommandDTO;
import com.paymybuddy.model.*;
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
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Controller
@Slf4j
public class ProfileController {

    private final UserAccountService userAccountService;
    private final BankAccountService bankAccountService;
    private final BankTransactionService bankTransactionService;
    private final BalanceByCurrencyService balanceByCurrencyService;

    private final long TRANSACTION_PER_PAGE = 5;

    @Autowired
    public ProfileController(UserAccountService userAccountService, BankAccountService bankAccountService, BankTransactionService bankTransactionService, BalanceByCurrencyService balanceByCurrencyService) {
        this.userAccountService = userAccountService;
        this.bankAccountService = bankAccountService;
        this.bankTransactionService = bankTransactionService;
        this.balanceByCurrencyService = balanceByCurrencyService;
    }

    @GetMapping("/profile")
    public String getProfile(
            Principal principal,
            Model model,
            @RequestParam(name = "page", required = false) Integer page) {
        UserAccount connectedUser = userAccountService.getUserWithEmail(principal.getName()).orElse(null);
        List<BankAccount> currentBankAccounts = bankAccountService.getBankAccountsFor(connectedUser);
        List<BankTransaction> bankTransactions = bankTransactionService.fetchTransactionsFor(connectedUser);

        long pageToShow = page == null ? 1 : page;
        long numberOfPages = (long) Math.ceil((double) bankTransactions.size() / TRANSACTION_PER_PAGE);
        numberOfPages = numberOfPages == 0 ? 1 : numberOfPages;

        List<BankTransaction> bankTransactionsToShow = bankTransactions.stream()
                .skip(TRANSACTION_PER_PAGE * (pageToShow - 1))
                .limit(TRANSACTION_PER_PAGE)
                .toList();

        List<BalanceByCurrency> balanceByCurrencies = balanceByCurrencyService.fetchBalanceByCurrencyFor(connectedUser);
        List<Currency> allCurrencies = Arrays.asList(Currency.values());

        model.addAttribute("bankAccounts", currentBankAccounts);
        model.addAttribute("bankTransactionCommand", new BankTransactionCommandDTO());
        model.addAttribute("bankTransactions", bankTransactionsToShow);
        model.addAttribute("balanceByCurrencies", balanceByCurrencies);
        model.addAttribute("numberOfPages", numberOfPages);
        model.addAttribute("currentPage", pageToShow);
        model.addAttribute("allCurrencies", allCurrencies);

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
            Principal principal,
            RedirectAttributes ra
    ) {
        UserAccount connectedUser = userAccountService.getUserWithEmail(principal.getName()).orElse(null);
        try {
            bankAccountService.create(new BankAccountCreationCommandDTO(connectedUser, bankAccount.getIban(), bankAccount.getCountry()));
        } catch (EmptyFieldException e) {
            ra.addFlashAttribute("emptyFieldError", true);
            return "redirect:/addBankAccount";
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
        double transactionAmount = action.equals("addMoney") ? bankTransactionCommandDTO.getAmount() : -bankTransactionCommandDTO.getAmount();

        if (transactionAmount == 0) {
            ra.addFlashAttribute("zeroMoney", true);
            return "redirect:/profile";
        }

        try {
            bankTransactionService.newTransaction(new BankTransactionCommandDTO(
                    bankAccount,
                    transactionAmount,
                    bankTransactionCommandDTO.getCurrency()
            ));
        } catch (BalanceAndTransferException e) {
            log.error(e.getMessage());
            ra.addFlashAttribute("notEnoughMoney", true);
        } catch (Exception e) {
            log.error(e.getMessage());
        }

        return "redirect:/profile";
    }
}
