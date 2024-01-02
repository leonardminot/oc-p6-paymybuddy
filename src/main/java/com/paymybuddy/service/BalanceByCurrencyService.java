package com.paymybuddy.service;

import com.paymybuddy.Exception.BalanceAndTransferException;
import com.paymybuddy.model.BalanceByCurrency;
import com.paymybuddy.model.UserAccount;
import com.paymybuddy.dto.BankTransactionCommandDTO;
import com.paymybuddy.dto.UserTransactionCommand;
import com.paymybuddy.repository.definition.BalanceByCurrencyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
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
                userTransactionCommand.getFromUser(),
                userTransactionCommand.getCurrency()
        );

        if (fromUserBalanceByCurrency.isPresent()) {
            BalanceByCurrency currentBalanceByCurrency = fromUserBalanceByCurrency.get();

            throwIfProjectedAmountBeyondZero(-userTransactionCommand.getAmount(), fromUserBalanceByCurrency);

            updateBalanceByCurrencyWithNewAmount(new BalanceByCurrency(
                    currentBalanceByCurrency.getBalanceID(),
                    currentBalanceByCurrency.getUserAccount(),
                    currentBalanceByCurrency.getBalance() - userTransactionCommand.getAmount(),
                    currentBalanceByCurrency.getCurrency()
            ));
        } else {
            throwIfNoBalanceByCurrencyForFromUserFound();
        }
    }

    public void createOrUpdateAssociatedBalanceByCurrencyForBankTransaction(BankTransactionCommandDTO bankTransactionCommand) {
        Optional<BalanceByCurrency> existingBalanceByCurrency = getByUserAccountAndCurrency(
                bankTransactionCommand.getBankAccount().getUserAccount(),
                bankTransactionCommand.getCurrency());

        throwIfProjectedAmountBeyondZero(bankTransactionCommand.getAmount(), existingBalanceByCurrency);

        BalanceByCurrency balanceModel = existingBalanceByCurrency
                .map(balance -> createBalance(balance, bankTransactionCommand.getAmount()))
                .orElseGet(() -> createBalance(bankTransactionCommand));
        updateBalanceByCurrencyWithNewAmount(balanceModel);
    }

    public void updateOrCreateToUserBalanceByCurrency(UserTransactionCommand userTransactionCommand) {
        Optional<BalanceByCurrency> toUserBalanceByCurrency = balanceByCurrencyRepository.getByUserAccountAndCurrency(
                userTransactionCommand.getToUser(),
                userTransactionCommand.getCurrency()
        );

        if (toUserBalanceByCurrency.isPresent()) {
            BalanceByCurrency balanceByCurrencyModel = toUserBalanceByCurrency.get();

            updateBalanceByCurrencyWithNewAmount(new BalanceByCurrency(
                    balanceByCurrencyModel.getBalanceID(),
                    balanceByCurrencyModel.getUserAccount(),
                    balanceByCurrencyModel.getBalance() + userTransactionCommand.getAmount(),
                    balanceByCurrencyModel.getCurrency()
            ));

        } else {
            updateBalanceByCurrencyWithNewAmount(new BalanceByCurrency(
                    UUID.fromString("00000000-0000-0000-0000-000000000000"),
                    userTransactionCommand.getToUser(),
                    userTransactionCommand.getAmount(),
                    userTransactionCommand.getCurrency()
            ));
        }
    }

    public List<BalanceByCurrency> fetchBalanceByCurrencyFor(UserAccount targetUser) {
        return balanceByCurrencyRepository.getForUser(targetUser);
    }

    private void throwIfNoBalanceByCurrencyForFromUserFound() {
        throw new BalanceAndTransferException("No BalanceByCurrency for From User found");
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
                bankTransactionCommand.getBankAccount().getUserAccount(),
                bankTransactionCommand.getAmount(),
                bankTransactionCommand.getCurrency()
        );
    }

    private void throwIfProjectedAmountBeyondZero(double amount, Optional<BalanceByCurrency> existingBalanceByCurrency) {
        double projectedAmount = amount;

        if (existingBalanceByCurrency.isPresent()) {
            projectedAmount  += existingBalanceByCurrency.get().getBalance();
        }

        if (projectedAmount < 0) {
            throw new BalanceAndTransferException("Amount can not go beyond 0");
        }
    }
}
