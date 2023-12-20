package com.paymybuddy.utils;

import com.paymybuddy.model.BankAccount;
import com.paymybuddy.model.UserAccount;
import com.paymybuddy.repository.definition.BankAccountRepository;

import java.util.ArrayList;
import java.util.List;

public class FakeBankAccountRepository implements BankAccountRepository {
    List<BankAccount> bankAccounts = new ArrayList<>();
    @Override
    public void save(BankAccount bankAccount) {
        bankAccounts.add(bankAccount);
    }

    @Override
    public BankAccount get(BankAccount bankAccount) {
        return bankAccounts.stream()
                .filter(ba -> ba.equals(bankAccount))
                .findAny()
                .orElse(null);
    }

    @Override
    public boolean isIBANExists(String iban) {
        return bankAccounts.stream()
                .anyMatch(ba -> ba.getIban().equals(iban));
    }

    @Override
    public List<BankAccount> fetchAllBankAccountsForUser(UserAccount user) {
        return bankAccounts.stream().filter(bankAccount -> bankAccount.getUserAccount().equals(user)).toList();
    }
}
