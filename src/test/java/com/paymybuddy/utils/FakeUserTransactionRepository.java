package com.paymybuddy.utils;

import com.paymybuddy.domain.model.TransactionModel;
import com.paymybuddy.domain.repository.UserTransactionRepository;

import java.util.ArrayList;
import java.util.List;

public class FakeUserTransactionRepository implements UserTransactionRepository {
    List<TransactionModel> transactions = new ArrayList<>();
    @Override
    public TransactionModel save(TransactionModel transactionModel) {
        transactions.add(transactionModel);
        return transactionModel;
    }

    @Override
    public List<TransactionModel> getAll() {
        return transactions;
    }

    @Override
    public TransactionModel get(TransactionModel transaction) {
        return transactions.stream()
                .filter(tr -> tr.equals(transaction))
                .findAny()
                .orElse(null);
    }
}
