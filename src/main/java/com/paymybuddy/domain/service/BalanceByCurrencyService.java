package com.paymybuddy.domain.service;

import com.paymybuddy.domain.dto.BankTransactionCommandDTO;
import com.paymybuddy.domain.dto.UserTransactionCommand;
import com.paymybuddy.domain.model.BalanceByCurrencyModel;
import com.paymybuddy.domain.model.UserAccountModel;
import com.paymybuddy.domain.repository.BalanceByCurrencyRepository;

import java.util.Optional;

public class BalanceByCurrencyService {
    private final BalanceByCurrencyRepository balanceByCurrencyRepository;

    public BalanceByCurrencyService(BalanceByCurrencyRepository balanceByCurrencyRepository) {
        this.balanceByCurrencyRepository = balanceByCurrencyRepository;
    }

    public void updateBalanceByCurrencyForFromUserOrThrowIfInsufficientAmount(UserTransactionCommand userTransactionCommand) {
        Optional<BalanceByCurrencyModel> fromUserBalanceByCurrency = getByUserAccountAndCurrency(
                userTransactionCommand.fromUser(),
                userTransactionCommand.currency()
        );

        if (fromUserBalanceByCurrency.isPresent()) {
            BalanceByCurrencyModel currentBalanceByCurrency = fromUserBalanceByCurrency.get();

            throwIfProjectedAmountBeyondZero(-userTransactionCommand.amount(), fromUserBalanceByCurrency);

            updateBalanceByCurrencyWithNewAmount(new BalanceByCurrencyModel(
                    currentBalanceByCurrency.id(),
                    currentBalanceByCurrency.userAccount(),
                    currentBalanceByCurrency.balance() - userTransactionCommand.amount(),
                    currentBalanceByCurrency.currency()
            ));
        } else {
            throwIfNoBalanceByCurrencyForFromUserFound();
        }
    }

    public void createOrUpdateAssociatedBalanceByCurrencyForBankTransaction(BankTransactionCommandDTO bankTransactionCommand) {
        Optional<BalanceByCurrencyModel> existingBalanceByCurrency = getByUserAccountAndCurrency(
                bankTransactionCommand.bankAccount().userAccount(),
                bankTransactionCommand.currency());

        throwIfProjectedAmountBeyondZero(bankTransactionCommand.amount(), existingBalanceByCurrency);

        BalanceByCurrencyModel balanceModel = existingBalanceByCurrency
                .map(balance -> createBalance(balance, bankTransactionCommand.amount()))
                .orElseGet(() -> createBalance(bankTransactionCommand));
        updateBalanceByCurrencyWithNewAmount(balanceModel);
    }

    public void updateOrCreateToUserBalanceByCurrency(UserTransactionCommand userTransactionCommand) {
        Optional<BalanceByCurrencyModel> toUserBalanceByCurrency = balanceByCurrencyRepository.getByUserAccountAndCurrency(
                userTransactionCommand.toUser(),
                userTransactionCommand.currency()
        );

        if (toUserBalanceByCurrency.isPresent()) {
            BalanceByCurrencyModel balanceByCurrencyModel = toUserBalanceByCurrency.get();

            updateBalanceByCurrencyWithNewAmount(new BalanceByCurrencyModel(
                    balanceByCurrencyModel.id(),
                    balanceByCurrencyModel.userAccount(),
                    balanceByCurrencyModel.balance() + userTransactionCommand.amount(),
                    balanceByCurrencyModel.currency()
            ));

        } else {
            updateBalanceByCurrencyWithNewAmount(new BalanceByCurrencyModel(
                    null,
                    userTransactionCommand.toUser(),
                    userTransactionCommand.amount(),
                    userTransactionCommand.currency()
            ));
        }
    }

    private void throwIfNoBalanceByCurrencyForFromUserFound() {
        throw new RuntimeException("No BalanceByCurrency for From User found");
    }

    private void updateBalanceByCurrencyWithNewAmount(BalanceByCurrencyModel currentBalanceByCurrency) {
        balanceByCurrencyRepository.save(currentBalanceByCurrency);
    }

    private Optional<BalanceByCurrencyModel> getByUserAccountAndCurrency(UserAccountModel user, String currency) {
        return balanceByCurrencyRepository.getByUserAccountAndCurrency(user, currency);
    }

    private BalanceByCurrencyModel createBalance(BalanceByCurrencyModel existingBalance, double amount) {
        return new BalanceByCurrencyModel(
                existingBalance.id(),
                existingBalance.userAccount(),
                existingBalance.balance() + amount,
                existingBalance.currency()
        );
    }

    private BalanceByCurrencyModel createBalance(BankTransactionCommandDTO bankTransactionCommand) {
        return new BalanceByCurrencyModel(
                null,
                bankTransactionCommand.bankAccount().userAccount(),
                bankTransactionCommand.amount(),
                bankTransactionCommand.currency()
        );
    }

    private void throwIfProjectedAmountBeyondZero(double amount, Optional<BalanceByCurrencyModel> existingBalanceByCurrency) {
        double projectedAmount = amount;

        if (existingBalanceByCurrency.isPresent()) {
            projectedAmount  += existingBalanceByCurrency.get().balance();
        }

        if (projectedAmount < 0) {
            throw new RuntimeException("Amount can not go beyond 0");
        }
    }

}
