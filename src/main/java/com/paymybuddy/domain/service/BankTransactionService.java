package com.paymybuddy.domain.service;

import com.paymybuddy.application.model.BankTransaction;
import com.paymybuddy.domain.dto.BankTransactionCommandDTO;
import com.paymybuddy.domain.repository.BankTransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class BankTransactionService {
    private final BalanceByCurrencyService balanceByCurrencyService;
    private final BankTransactionRepository bankTransactionRepository;
    private final DateProvider dateProvider;

    @Autowired
    public BankTransactionService(BalanceByCurrencyService balanceByCurrencyService, BankTransactionRepository bankTransactionRepository, DateProvider dateProvider) {
        this.balanceByCurrencyService = balanceByCurrencyService;
        this.bankTransactionRepository = bankTransactionRepository;
        this.dateProvider = dateProvider;
    }

    public void newTransaction(BankTransactionCommandDTO bankTransactionCommand) {
        throwIfNullData(bankTransactionCommand);
        balanceByCurrencyService.createOrUpdateAssociatedBalanceByCurrencyForBankTransaction(bankTransactionCommand);
        saveANewTransaction(bankTransactionCommand);
    }

    private void throwIfNullData(BankTransactionCommandDTO bankTransactionCommand) {
        throwIfUserAccountIsNull(bankTransactionCommand);
        throwIfCurrencyIsNull(bankTransactionCommand);
    }

    private void throwIfUserAccountIsNull(BankTransactionCommandDTO bankTransactionCommand) {
        if (bankTransactionCommand.bankAccount() == null)
            throw new RuntimeException("User Account must be not null");
    }

    private void throwIfCurrencyIsNull(BankTransactionCommandDTO bankTransactionCommand) {
        if (bankTransactionCommand.currency() == null)
            throw new RuntimeException("Currency must be not null");
    }

    private void saveANewTransaction(BankTransactionCommandDTO bankTransactionCommand) {
        bankTransactionRepository.save(new BankTransaction(
                UUID.fromString("00000000-0000-0000-0000-000000000000"),
                bankTransactionCommand.bankAccount(),
                bankTransactionCommand.amount(),
                bankTransactionCommand.currency(),
                dateProvider.getNow()
        ));
    }

}
