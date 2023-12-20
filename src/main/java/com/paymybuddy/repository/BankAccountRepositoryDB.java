package com.paymybuddy.repository;

import com.paymybuddy.model.BankAccount;
import com.paymybuddy.model.UserAccount;
import com.paymybuddy.repository.jpa.BankAccountRepositoryJpa;
import com.paymybuddy.repository.definition.BankAccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.StreamSupport;

@Repository
public class BankAccountRepositoryDB implements BankAccountRepository {

    private final BankAccountRepositoryJpa bankAccountRepositoryJpa;

    @Autowired
    public BankAccountRepositoryDB(BankAccountRepositoryJpa bankAccountRepositoryJpa) {
        this.bankAccountRepositoryJpa = bankAccountRepositoryJpa;
    }

    @Override
    public void save(BankAccount bankAccount) {
        bankAccountRepositoryJpa.save(bankAccount);
    }

    @Override
    public BankAccount get(BankAccount bankAccount) {
        return bankAccountRepositoryJpa.findById(bankAccount.getBankAccountId()).orElse(null);
    }

    @Override
    public boolean isIBANExists(String iban) {
        return bankAccountRepositoryJpa.findByIban(iban).isPresent();
    }

    @Override
    public List<BankAccount> fetchAllBankAccountsForUser(UserAccount user) {
        return StreamSupport.stream(bankAccountRepositoryJpa.findAll().spliterator(), false)
                .filter(bankAccount -> bankAccount.getUserAccount().equals(user))
                .toList();
    }
}
