package com.paymybuddy.domain.repository;

import com.paymybuddy.domain.dto.BankTransactionCommandDTO;
import com.paymybuddy.domain.model.BankTransactionModel;

import java.util.List;

public interface BankTransactionRepository {
    void save(BankTransactionModel bankTransactionCommand);
    BankTransactionModel get(BankTransactionModel bankTransaction);

    List<BankTransactionModel> getAll();
}
