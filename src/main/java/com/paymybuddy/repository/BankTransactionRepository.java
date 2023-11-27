package com.paymybuddy.repository;

import com.paymybuddy.model.BankTransaction;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface BankTransactionRepository extends CrudRepository<BankTransaction, UUID> {
}
