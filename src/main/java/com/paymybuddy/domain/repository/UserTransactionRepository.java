package com.paymybuddy.domain.repository;

import com.paymybuddy.domain.model.TransactionModel;

import java.util.List;

public interface UserTransactionRepository {
    TransactionModel save(TransactionModel transactionModel);
    List<TransactionModel> getAll();
    TransactionModel get(TransactionModel transaction);
}
