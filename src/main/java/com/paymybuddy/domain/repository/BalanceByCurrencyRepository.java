package com.paymybuddy.domain.repository;

import com.paymybuddy.application.model.BalanceByCurrency;
import com.paymybuddy.application.model.UserAccount;

import java.util.List;
import java.util.Optional;

public interface BalanceByCurrencyRepository {
    void save(BalanceByCurrency balanceByCurrency);
    BalanceByCurrency get(BalanceByCurrency balanceByCurrency);

    Optional<BalanceByCurrency> getByUserAccountAndCurrency(UserAccount userAccount, String currency);
    List<BalanceByCurrency> getAll();
}
