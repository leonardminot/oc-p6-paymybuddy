package com.paymybuddy.domain.repository;

import com.paymybuddy.application.model.BankTransaction;

import java.util.List;

public interface BankTransactionRepository {
    void save(BankTransaction bankTransactionCommand);
    BankTransaction get(BankTransaction bankTransaction);

    List<BankTransaction> getAll();
}
