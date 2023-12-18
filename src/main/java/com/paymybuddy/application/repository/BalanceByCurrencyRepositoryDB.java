package com.paymybuddy.application.repository;

import com.paymybuddy.application.model.BalanceByCurrency;
import com.paymybuddy.application.model.UserAccount;
import com.paymybuddy.application.repository.jpa.BalanceByCurrencyRepositoryJpa;
import com.paymybuddy.application.repository.definition.BalanceByCurrencyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;

@Repository
public class BalanceByCurrencyRepositoryDB implements BalanceByCurrencyRepository {

    private final BalanceByCurrencyRepositoryJpa balanceByCurrencyRepositoryJpa;

    @Autowired
    public BalanceByCurrencyRepositoryDB(BalanceByCurrencyRepositoryJpa balanceByCurrencyRepositoryJpa) {
        this.balanceByCurrencyRepositoryJpa = balanceByCurrencyRepositoryJpa;
    }

    @Override
    public void save(BalanceByCurrency balanceByCurrency) {
        balanceByCurrencyRepositoryJpa.save(balanceByCurrency);
    }

    @Override
    public BalanceByCurrency get(BalanceByCurrency balanceByCurrency) {
        return balanceByCurrencyRepositoryJpa.findById(balanceByCurrency.getBalanceID()).orElse(null);
    }

    @Override
    public Optional<BalanceByCurrency> getByUserAccountAndCurrency(UserAccount userAccount, String currency) {
        return balanceByCurrencyRepositoryJpa.findByUserAccountAndCurrency(userAccount, currency);
    }

    @Override
    public List<BalanceByCurrency> getAll() {
        Iterable<BalanceByCurrency> allBalanceByCurrencies = balanceByCurrencyRepositoryJpa.findAll();
        return StreamSupport.stream(allBalanceByCurrencies.spliterator(), false).toList();
    }
}
