package com.paymybuddy.utils;

import com.paymybuddy.model.BankTransaction;
import com.paymybuddy.repository.definition.BankTransactionRepository;

import java.util.ArrayList;
import java.util.List;

public class FakeBankTransactionRepository implements BankTransactionRepository {

    List<BankTransaction> bankTransactions = new ArrayList<>();
    @Override
    public void save(BankTransaction bankTransaction) {
        bankTransactions.add(bankTransaction);
    }

    @Override
    public BankTransaction get(BankTransaction bankTransaction) {
        return bankTransactions.stream()
                .filter(bt -> bt.equals(bankTransaction))
                .findAny()
                .orElse(null);
    }

    @Override
    public List<BankTransaction> getAll() {
        return bankTransactions;
    }
}
