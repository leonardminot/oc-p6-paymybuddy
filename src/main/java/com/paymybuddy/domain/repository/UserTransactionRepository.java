package com.paymybuddy.domain.repository;

import com.paymybuddy.application.model.Transaction;

import java.util.List;

public interface UserTransactionRepository {
    Transaction save(Transaction transactionModel);
    List<Transaction> getAll();
    Transaction get(Transaction transaction);
}
