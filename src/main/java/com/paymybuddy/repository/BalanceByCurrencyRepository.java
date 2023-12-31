package com.paymybuddy.repository;

import com.paymybuddy.model.BalanceByCurrency;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface BalanceByCurrencyRepository extends CrudRepository<BalanceByCurrency, UUID> {
}
