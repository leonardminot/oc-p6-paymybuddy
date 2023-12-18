package com.paymybuddy.repository;

import com.paymybuddy.model.Transaction;
import com.paymybuddy.repository.jpa.TransactionRepositoryJpa;
import com.paymybuddy.repository.definition.UserTransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class TransactionRepositoryDB implements UserTransactionRepository {
    private final TransactionRepositoryJpa transactionRepositoryJpa;

    @Autowired
    public TransactionRepositoryDB(TransactionRepositoryJpa transactionRepositoryJpa) {
        this.transactionRepositoryJpa = transactionRepositoryJpa;
    }

    @Override
    public Transaction save(Transaction transactionModel) {
        return transactionRepositoryJpa.save(transactionModel);
    }

    @Override
    public List<Transaction> getAll() {
        List<Transaction> allTransactions = transactionRepositoryJpa.findAll();
        return allTransactions.stream().toList();
    }

    @Override
    public Transaction get(Transaction transaction) {
        return transactionRepositoryJpa.findById(transaction.getTransactionID()).orElse(null);
    }
}
