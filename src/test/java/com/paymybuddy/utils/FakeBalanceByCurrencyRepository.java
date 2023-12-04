package com.paymybuddy.utils;

import com.paymybuddy.domain.model.BalanceByCurrencyModel;
import com.paymybuddy.domain.model.UserAccountModel;
import com.paymybuddy.domain.repository.BalanceByCurrencyRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class FakeBalanceByCurrencyRepository implements BalanceByCurrencyRepository {
    List<BalanceByCurrencyModel> balanceByCurrencies = new ArrayList<>();

    @Override
    public void save(BalanceByCurrencyModel balanceByCurrency) {
        Optional<BalanceByCurrencyModel> existingBalanceByCurrency = balanceByCurrencies.stream()
                .filter(bbc -> bbc.id().equals(balanceByCurrency.id()))
                .findAny();

        if (existingBalanceByCurrency.isPresent()) {
            BalanceByCurrencyModel existingBalance = existingBalanceByCurrency.get();

            BalanceByCurrencyModel updatedBalance = new BalanceByCurrencyModel(
                    existingBalance.id(),
                    existingBalance.userAccount(),
                    existingBalance.balance() + balanceByCurrency.balance(),
                    existingBalance.currency()
            );

            balanceByCurrencies.set(balanceByCurrencies.indexOf(existingBalance), updatedBalance);
        } else {
            balanceByCurrencies.add(balanceByCurrency);
        }
    }

    @Override
    public BalanceByCurrencyModel get(BalanceByCurrencyModel balanceByCurrency) {
        return balanceByCurrencies.stream()
                .filter(bbc -> bbc.equals(balanceByCurrency))
                .findAny()
                .orElse(null);
    }

    @Override
    public Optional<BalanceByCurrencyModel> getByUserAccountAndCurrency(UserAccountModel userAccountModel, String currency) {
        return balanceByCurrencies.stream()
                .filter(bbc -> bbc.userAccount().equals(userAccountModel) && bbc.currency().equals(currency))
                .findAny();
    }

    @Override
    public List<BalanceByCurrencyModel> getAll() {
        return balanceByCurrencies;
    }
}
