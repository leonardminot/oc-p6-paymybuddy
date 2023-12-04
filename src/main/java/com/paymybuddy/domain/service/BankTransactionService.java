package com.paymybuddy.domain.service;

import com.paymybuddy.domain.dto.BankTransactionCommandDTO;
import com.paymybuddy.domain.model.BalanceByCurrencyModel;
import com.paymybuddy.domain.model.BankTransactionModel;
import com.paymybuddy.domain.repository.BalanceByCurrencyRepository;
import com.paymybuddy.domain.repository.BankTransactionRepository;

import java.util.Optional;

public class BankTransactionService {
    private final BankTransactionRepository bankTransactionRepository;
    private final BalanceByCurrencyRepository balanceByCurrencyRepository;
    private final DateProvider dateProvider;

    public BankTransactionService(BankTransactionRepository bankTransactionRepository, BalanceByCurrencyRepository balanceByCurrencyRepository, DateProvider dateProvider) {
        this.bankTransactionRepository = bankTransactionRepository;
        this.balanceByCurrencyRepository = balanceByCurrencyRepository;
        this.dateProvider = dateProvider;
    }

    public void newTransaction(BankTransactionCommandDTO bankTransactionCommand) {
        throwIfNullData(bankTransactionCommand);

        Optional<BalanceByCurrencyModel> existingBalanceByCurrency = balanceByCurrencyRepository.getByUserAccountAndCurrency(
                bankTransactionCommand.bankAccount().userAccount(),
                bankTransactionCommand.currency());

        throwIfProjectedAmountBeyondZero(bankTransactionCommand, existingBalanceByCurrency);

        createOrUpdateAssociatedBalanceByCurrency(bankTransactionCommand, existingBalanceByCurrency);
        saveANewTransaction(bankTransactionCommand);
    }

    private void throwIfProjectedAmountBeyondZero(BankTransactionCommandDTO bankTransactionCommand, Optional<BalanceByCurrencyModel> existingBalanceByCurrency) {
        double projectedAmount = bankTransactionCommand.amount();

        if (existingBalanceByCurrency.isPresent()) {
            projectedAmount  += existingBalanceByCurrency.get().balance();
        }

        if (projectedAmount < 0) {
            throw new RuntimeException("Amount can not go beyond 0");
        }
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

    private void createOrUpdateAssociatedBalanceByCurrency(BankTransactionCommandDTO bankTransactionCommand,
                                                           Optional<BalanceByCurrencyModel> existingBalanceByCurrency) {
        BalanceByCurrencyModel balanceModel = existingBalanceByCurrency
                .map(balance -> createBalanceModel(balance, bankTransactionCommand.amount()))
                .orElseGet(() -> createBalanceModel(bankTransactionCommand));
        balanceByCurrencyRepository.save(balanceModel);
    }

    private BalanceByCurrencyModel createBalanceModel(BalanceByCurrencyModel existingBalance, double amount) {
        return new BalanceByCurrencyModel(
                existingBalance.id(),
                existingBalance.userAccount(),
                existingBalance.balance() + amount,
                existingBalance.currency()
        );
    }

    private BalanceByCurrencyModel createBalanceModel(BankTransactionCommandDTO bankTransactionCommand) {
        return new BalanceByCurrencyModel(
                null,
                bankTransactionCommand.bankAccount().userAccount(),
                bankTransactionCommand.amount(),
                bankTransactionCommand.currency()
        );
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
