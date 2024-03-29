package com.paymybuddy.controller;

import com.paymybuddy.Exception.BalanceAndTransferException;
import com.paymybuddy.dto.UserTransactionCommand;
import com.paymybuddy.dto.UserTransactionDTO;
import com.paymybuddy.model.Currency;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.util.Arrays;
import java.util.List;

@Controller
@Slf4j
public class TransferController {

    private final UserAccountService userAccountService;
    private final UserRelationService userRelationService;
    private final UserTransactionService userTransactionService;
    private final long TRANSACTION_PER_PAGE = 5;

    @Autowired
    public TransferController(UserAccountService userAccountService, UserRelationService userRelationService, UserTransactionService userTransactionService) {
        this.userAccountService = userAccountService;
        this.userRelationService = userRelationService;
        this.userTransactionService = userTransactionService;
    }

    @GetMapping("/transfer")
    public String transferPage(
            Principal principal,
            Model model,
            @RequestParam(name = "page", required = false) Integer page) {

        UserAccount connectedUser = userAccountService.getUserWithEmail(principal.getName()).orElse(null);

        model.addAttribute("relations", userRelationService.getRelationsFor(connectedUser));
        model.addAttribute("transferCommand", new UserTransactionCommand());
        model.addAttribute("transactions", getTransactionsToShow(userTransactionService.getTransactionsFor(connectedUser), getPageToShow(page)));
        model.addAttribute("connectedUser", connectedUser);
        model.addAttribute("numberOfPages", getNumberOfPages(userTransactionService.getTransactionsFor(connectedUser)));
        model.addAttribute("currentPage", getPageToShow(page));
        model.addAttribute("allCurrencies", Arrays.asList(Currency.values()));

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
        } catch (BalanceAndTransferException e) {
            ra.addFlashAttribute("transactionError", true);
            log.error(e.getMessage());
        } catch (Exception e) {
            ra.addFlashAttribute("hasError", true);
            log.error(e.getMessage());
        }
        return "redirect:/transfer";
    }

    private long getPageToShow(Integer page) {
        return page == null ? 1 : page;
    }

    private long getNumberOfPages(List<UserTransactionDTO> transactions) {
        long numberOfPages = (long) Math.ceil((double) transactions.size() / TRANSACTION_PER_PAGE);
        numberOfPages = numberOfPages == 0 ? 1 : numberOfPages;
        return numberOfPages;
    }

    private List<UserTransactionDTO> getTransactionsToShow(List<UserTransactionDTO> transactions, long pageToShow) {
        return transactions.stream()
                .skip(TRANSACTION_PER_PAGE * (pageToShow - 1))
                .limit(TRANSACTION_PER_PAGE)
                .toList();
    }
}
