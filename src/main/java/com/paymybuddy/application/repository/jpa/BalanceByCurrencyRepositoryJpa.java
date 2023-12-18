package com.paymybuddy.application.repository.jpa;

import com.paymybuddy.application.model.BalanceByCurrency;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface BalanceByCurrencyRepositoryJpa extends CrudRepository<BalanceByCurrency, UUID> {
}
