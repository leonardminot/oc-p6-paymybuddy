package com.paymybuddy.application.repository;

import com.paymybuddy.application.model.BankTransaction;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface BankTransactionRepositoryJpa extends CrudRepository<BankTransaction, UUID> {
}
