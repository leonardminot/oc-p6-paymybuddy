package com.paymybuddy.service;

import com.paymybuddy.Exception.EmptyFieldException;
import com.paymybuddy.Exception.UserException;
import com.paymybuddy.model.BankAccount;
import com.paymybuddy.model.BankTransaction;
import com.paymybuddy.dto.BankTransactionCommandDTO;
import com.paymybuddy.model.UserAccount;
import com.paymybuddy.repository.definition.BankTransactionRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;

@Service
public class BankTransactionService {
    private final BalanceByCurrencyService balanceByCurrencyService;

    private final BankAccountService bankAccountService;
    private final BankTransactionRepository bankTransactionRepository;
    private final DateProvider dateProvider;

    @Autowired
    public BankTransactionService(BalanceByCurrencyService balanceByCurrencyService, BankAccountService bankAccountService, BankTransactionRepository bankTransactionRepository, DateProvider dateProvider) {
        this.balanceByCurrencyService = balanceByCurrencyService;
        this.bankAccountService = bankAccountService;
        this.bankTransactionRepository = bankTransactionRepository;
        this.dateProvider = dateProvider;
    }

    @Transactional
    public void newTransaction(BankTransactionCommandDTO bankTransactionCommand) {
        throwIfNullData(bankTransactionCommand);
        balanceByCurrencyService.createOrUpdateAssociatedBalanceByCurrencyForBankTransaction(bankTransactionCommand);
        saveANewTransaction(bankTransactionCommand);
    }

    public List<BankTransaction> fetchTransactionsFor(UserAccount targetUser) {
        List<BankAccount> targetUserBankAccounts = bankAccountService.getBankAccountsFor(targetUser);
        List<BankTransaction> bankTransactionsForTargetUser = new ArrayList<>();
        for (BankAccount bankAccount : targetUserBankAccounts) {
            bankTransactionsForTargetUser.addAll(bankTransactionRepository.getAllFor(bankAccount));
        }
        return bankTransactionsForTargetUser.stream()
                .sorted(Comparator.comparing(BankTransaction::getDate).reversed())
                .toList();
    }

    private void throwIfNullData(BankTransactionCommandDTO bankTransactionCommand) {
        throwIfUserAccountIsNull(bankTransactionCommand);
        throwIfCurrencyIsNull(bankTransactionCommand);
    }

    private void throwIfUserAccountIsNull(BankTransactionCommandDTO bankTransactionCommand) {
        if (bankTransactionCommand.getBankAccount()== null)
            throw new UserException("User Account must be not null");
    }

    private void throwIfCurrencyIsNull(BankTransactionCommandDTO bankTransactionCommand) {
        if (bankTransactionCommand.getCurrency() == null)
            throw new EmptyFieldException("Currency must be not null");
    }

    private void saveANewTransaction(BankTransactionCommandDTO bankTransactionCommand) {
        bankTransactionRepository.save(new BankTransaction(
                UUID.fromString("00000000-0000-0000-0000-000000000000"),
                bankTransactionCommand.getBankAccount(),
                bankTransactionCommand.getAmount(),
                bankTransactionCommand.getCurrency(),
                dateProvider.getNow()
        ));
    }
}
