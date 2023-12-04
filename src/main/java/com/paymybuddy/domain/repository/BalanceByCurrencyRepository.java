package com.paymybuddy.domain.repository;

import com.paymybuddy.domain.model.BalanceByCurrencyModel;
import com.paymybuddy.domain.model.UserAccountModel;

import java.util.List;
import java.util.Optional;

public interface BalanceByCurrencyRepository {
    void save(BalanceByCurrencyModel balanceByCurrency);
    BalanceByCurrencyModel get(BalanceByCurrencyModel balanceByCurrency);

    Optional<BalanceByCurrencyModel> getByUserAccountAndCurrency(UserAccountModel userAccountModel, String currency);
    List<BalanceByCurrencyModel> getAll();
}
