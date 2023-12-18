package com.paymybuddy.utils;

import com.paymybuddy.application.model.BalanceByCurrency;
import com.paymybuddy.application.model.UserAccount;
import com.paymybuddy.domain.repository.BalanceByCurrencyRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class FakeBalanceByCurrencyRepository implements BalanceByCurrencyRepository {
    List<BalanceByCurrency> balanceByCurrencies = new ArrayList<>();

    @Override
    public void save(BalanceByCurrency balanceByCurrency) {
        Optional<BalanceByCurrency> existingBalanceByCurrency = balanceByCurrencies.stream()
                .filter(bbc -> bbc.getBalanceID().equals(balanceByCurrency.getBalanceID()))
                .findAny();

        if (existingBalanceByCurrency.isPresent()) {
            BalanceByCurrency existingBalance = existingBalanceByCurrency.get();

            BalanceByCurrency updatedBalance = new BalanceByCurrency(
                    existingBalance.getBalanceID(),
                    existingBalance.getUserAccount(),
                    balanceByCurrency.getBalance(),
                    existingBalance.getCurrency()
            );

            balanceByCurrencies.set(balanceByCurrencies.indexOf(existingBalance), updatedBalance);
        } else {
            balanceByCurrencies.add(balanceByCurrency);
        }
    }

    @Override
    public BalanceByCurrency get(BalanceByCurrency balanceByCurrency) {
        return balanceByCurrencies.stream()
                .filter(bbc -> bbc.equals(balanceByCurrency))
                .findAny()
                .orElse(null);
    }

    @Override
    public Optional<BalanceByCurrency> getByUserAccountAndCurrency(UserAccount userAccountModel, String currency) {
        return balanceByCurrencies.stream()
                .filter(bbc -> bbc.getUserAccount().equals(userAccountModel) && bbc.getCurrency().equals(currency))
                .findAny();
    }

    @Override
    public List<BalanceByCurrency> getAll() {
        return balanceByCurrencies;
    }
}
