package com.paymybuddy.domain.service;

import com.paymybuddy.application.model.BalanceByCurrency;
import com.paymybuddy.application.model.UserAccount;
import com.paymybuddy.domain.dto.BankTransactionCommandDTO;
import com.paymybuddy.domain.dto.UserTransactionCommand;
import com.paymybuddy.application.repository.definition.BalanceByCurrencyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class BalanceByCurrencyService {
    private final BalanceByCurrencyRepository balanceByCurrencyRepository;

    @Autowired
    public BalanceByCurrencyService(BalanceByCurrencyRepository balanceByCurrencyRepository) {
        this.balanceByCurrencyRepository = balanceByCurrencyRepository;
    }

    public void updateBalanceByCurrencyForFromUserOrThrowIfInsufficientAmount(UserTransactionCommand userTransactionCommand) {
        Optional<BalanceByCurrency> fromUserBalanceByCurrency = getByUserAccountAndCurrency(
                userTransactionCommand.fromUser(),
                userTransactionCommand.currency()
        );

        if (fromUserBalanceByCurrency.isPresent()) {
            BalanceByCurrency currentBalanceByCurrency = fromUserBalanceByCurrency.get();

            throwIfProjectedAmountBeyondZero(-userTransactionCommand.amount(), fromUserBalanceByCurrency);

            updateBalanceByCurrencyWithNewAmount(new BalanceByCurrency(
                    currentBalanceByCurrency.getBalanceID(),
                    currentBalanceByCurrency.getUserAccount(),
                    currentBalanceByCurrency.getBalance() - userTransactionCommand.amount(),
                    currentBalanceByCurrency.getCurrency()
            ));
        } else {
            throwIfNoBalanceByCurrencyForFromUserFound();
        }
    }

    public void createOrUpdateAssociatedBalanceByCurrencyForBankTransaction(BankTransactionCommandDTO bankTransactionCommand) {
        Optional<BalanceByCurrency> existingBalanceByCurrency = getByUserAccountAndCurrency(
                bankTransactionCommand.bankAccount().getUserAccount(),
                bankTransactionCommand.currency());

        throwIfProjectedAmountBeyondZero(bankTransactionCommand.amount(), existingBalanceByCurrency);

        BalanceByCurrency balanceModel = existingBalanceByCurrency
                .map(balance -> createBalance(balance, bankTransactionCommand.amount()))
                .orElseGet(() -> createBalance(bankTransactionCommand));
        updateBalanceByCurrencyWithNewAmount(balanceModel);
    }

    public void updateOrCreateToUserBalanceByCurrency(UserTransactionCommand userTransactionCommand) {
        Optional<BalanceByCurrency> toUserBalanceByCurrency = balanceByCurrencyRepository.getByUserAccountAndCurrency(
                userTransactionCommand.toUser(),
                userTransactionCommand.currency()
        );

        if (toUserBalanceByCurrency.isPresent()) {
            BalanceByCurrency balanceByCurrencyModel = toUserBalanceByCurrency.get();

            updateBalanceByCurrencyWithNewAmount(new BalanceByCurrency(
                    balanceByCurrencyModel.getBalanceID(),
                    balanceByCurrencyModel.getUserAccount(),
                    balanceByCurrencyModel.getBalance() + userTransactionCommand.amount(),
                    balanceByCurrencyModel.getCurrency()
            ));

        } else {
            updateBalanceByCurrencyWithNewAmount(new BalanceByCurrency(
                    UUID.fromString("00000000-0000-0000-0000-000000000000"),
                    userTransactionCommand.toUser(),
                    userTransactionCommand.amount(),
                    userTransactionCommand.currency()
            ));
        }
    }

    private void throwIfNoBalanceByCurrencyForFromUserFound() {
        throw new RuntimeException("No BalanceByCurrency for From User found");
    }

    private void updateBalanceByCurrencyWithNewAmount(BalanceByCurrency currentBalanceByCurrency) {
        balanceByCurrencyRepository.save(currentBalanceByCurrency);
    }

    private Optional<BalanceByCurrency> getByUserAccountAndCurrency(UserAccount user, String currency) {
        return balanceByCurrencyRepository.getByUserAccountAndCurrency(user, currency);
    }

    private BalanceByCurrency createBalance(BalanceByCurrency existingBalance, double amount) {
        return new BalanceByCurrency(
                existingBalance.getBalanceID(),
                existingBalance.getUserAccount(),
                existingBalance.getBalance() + amount,
                existingBalance.getCurrency()
        );
    }

    private BalanceByCurrency createBalance(BankTransactionCommandDTO bankTransactionCommand) {
        return new BalanceByCurrency(
                UUID.fromString("00000000-0000-0000-0000-000000000000"),
                bankTransactionCommand.bankAccount().getUserAccount(),
                bankTransactionCommand.amount(),
                bankTransactionCommand.currency()
        );
    }

    private void throwIfProjectedAmountBeyondZero(double amount, Optional<BalanceByCurrency> existingBalanceByCurrency) {
        double projectedAmount = amount;

        if (existingBalanceByCurrency.isPresent()) {
            projectedAmount  += existingBalanceByCurrency.get().getBalance();
        }

        if (projectedAmount < 0) {
            throw new RuntimeException("Amount can not go beyond 0");
        }
    }

}
