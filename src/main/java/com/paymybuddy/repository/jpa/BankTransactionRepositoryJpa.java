package com.paymybuddy.repository.jpa;

import com.paymybuddy.model.BankAccount;
import com.paymybuddy.model.BankTransaction;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface BankTransactionRepositoryJpa extends CrudRepository<BankTransaction, UUID> {
    List<BankTransaction> findBankTransactionsByBankAccountEquals(BankAccount bankAccount);
}
