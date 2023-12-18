package com.paymybuddy.repository.jpa;

import com.paymybuddy.model.BankTransaction;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface BankTransactionRepositoryJpa extends CrudRepository<BankTransaction, UUID> {
}
