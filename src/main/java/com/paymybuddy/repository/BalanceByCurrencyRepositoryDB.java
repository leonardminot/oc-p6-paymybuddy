package com.paymybuddy.repository;

import com.paymybuddy.model.BalanceByCurrency;
import com.paymybuddy.model.UserAccount;
import com.paymybuddy.repository.jpa.BalanceByCurrencyRepositoryJpa;
import com.paymybuddy.repository.definition.BalanceByCurrencyRepository;
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

    @Override
    public List<BalanceByCurrency> getForUser(UserAccount userAccount) {
        return balanceByCurrencyRepositoryJpa.findByUserAccountEquals(userAccount);
    }
}
