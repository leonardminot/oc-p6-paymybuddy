package com.paymybuddy.utils;

import com.paymybuddy.application.model.Transaction;
import com.paymybuddy.domain.repository.UserTransactionRepository;

import java.util.ArrayList;
import java.util.List;

public class FakeUserTransactionRepository implements UserTransactionRepository {
    List<Transaction> transactions = new ArrayList<>();
    @Override
    public Transaction save(Transaction transactionModel) {
        transactions.add(transactionModel);
        return transactionModel;
    }

    @Override
    public List<Transaction> getAll() {
        return transactions;
    }

    @Override
    public Transaction get(Transaction transaction) {
        return transactions.stream()
                .filter(tr -> tr.equals(transaction))
                .findAny()
                .orElse(null);
    }
}
