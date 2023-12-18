package com.paymybuddy.application.repository.jpa;

import com.paymybuddy.application.model.BalanceByCurrency;
import com.paymybuddy.application.model.UserAccount;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface BalanceByCurrencyRepositoryJpa extends CrudRepository<BalanceByCurrency, UUID> {
    Optional<BalanceByCurrency> findByUserAccountAndCurrency(UserAccount userAccount, String currency);
}
