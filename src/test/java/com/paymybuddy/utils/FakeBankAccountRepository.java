package com.paymybuddy.utils;

import com.paymybuddy.model.BankAccount;
import com.paymybuddy.model.UserAccount;
import com.paymybuddy.repository.definition.BankAccountRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class FakeBankAccountRepository implements BankAccountRepository {
    List<BankAccount> bankAccounts = new ArrayList<>();
    @Override
    public BankAccount save(BankAccount bankAccount) {
        bankAccounts.add(bankAccount);
        return bankAccount;
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

    @Override
    public Optional<BankAccount> getById(UUID bankAccountId) {
        return bankAccounts.stream().filter(ba -> ba.getBankAccountId().equals(bankAccountId)).findAny();
    }
}
