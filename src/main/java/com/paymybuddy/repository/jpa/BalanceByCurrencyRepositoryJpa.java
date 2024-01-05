package com.paymybuddy.repository.jpa;

import com.paymybuddy.model.BalanceByCurrency;
import com.paymybuddy.model.Currency;
import com.paymybuddy.model.UserAccount;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface BalanceByCurrencyRepositoryJpa extends CrudRepository<BalanceByCurrency, UUID> {
    Optional<BalanceByCurrency> findByUserAccountAndCurrency(UserAccount userAccount, Currency currency);
    List<BalanceByCurrency> findByUserAccountEquals(UserAccount userAccount);
}
