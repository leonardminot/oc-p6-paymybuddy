package com.paymybuddy.domain.service;

import com.paymybuddy.domain.dto.BankTransactionCommandDTO;
import com.paymybuddy.domain.model.BankTransactionModel;
import com.paymybuddy.domain.repository.BankTransactionRepository;

public class BankTransactionService {
    private final BalanceByCurrencyService balanceByCurrencyService;
    private final BankTransactionRepository bankTransactionRepository;
    private final DateProvider dateProvider;

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
        bankTransactionRepository.save(new BankTransactionModel(
                null,
                bankTransactionCommand.bankAccount(),
                bankTransactionCommand.amount(),
                bankTransactionCommand.currency(),
                dateProvider.getNow()
        ));
    }

}
