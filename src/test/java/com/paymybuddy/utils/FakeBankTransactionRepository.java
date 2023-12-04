package com.paymybuddy.utils;

import com.paymybuddy.domain.dto.BankTransactionCommandDTO;
import com.paymybuddy.domain.model.BankTransactionModel;
import com.paymybuddy.domain.repository.BankTransactionRepository;

import java.util.ArrayList;
import java.util.List;

public class FakeBankTransactionRepository implements BankTransactionRepository {

    List<BankTransactionModel> bankTransactions = new ArrayList<>();
    @Override
    public void save(BankTransactionModel bankTransaction) {
        bankTransactions.add(bankTransaction);
    }

    @Override
    public BankTransactionModel get(BankTransactionModel bankTransaction) {
        return bankTransactions.stream()
                .filter(bt -> bt.equals(bankTransaction))
                .findAny()
                .orElse(null);
    }

    @Override
    public List<BankTransactionModel> getAll() {
        return bankTransactions;
    }
}
