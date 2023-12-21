package com.paymybuddy.repository.definition;

import com.paymybuddy.model.BalanceByCurrency;
import com.paymybuddy.model.UserAccount;

import java.util.List;
import java.util.Optional;

public interface BalanceByCurrencyRepository {
    void save(BalanceByCurrency balanceByCurrency);
    BalanceByCurrency get(BalanceByCurrency balanceByCurrency);

    Optional<BalanceByCurrency> getByUserAccountAndCurrency(UserAccount userAccount, String currency);
    List<BalanceByCurrency> getAll();
    List<BalanceByCurrency> getForUser(UserAccount userAccount);
}
