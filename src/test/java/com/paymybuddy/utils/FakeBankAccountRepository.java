package com.paymybuddy.utils;

import com.paymybuddy.domain.model.BankAccountModel;
import com.paymybuddy.domain.repository.BankAccountRepository;

import java.util.ArrayList;
import java.util.List;

public class FakeBankAccountRepository implements BankAccountRepository {
    List<BankAccountModel> bankAccounts = new ArrayList<>();
    @Override
    public void save(BankAccountModel bankAccount) {
        bankAccounts.add(bankAccount);
    }

    @Override
    public BankAccountModel get(BankAccountModel bankAccount) {
        return bankAccounts.stream()
                .filter(ba -> ba.equals(bankAccount))
                .findAny()
                .orElse(null);
    }

    @Override
    public boolean isIBANExists(String iban) {
        return bankAccounts.stream()
                .anyMatch(ba -> ba.IBAN().equals(iban));
    }
}
