package com.paymybuddy.application.repository;

import com.paymybuddy.application.model.BankAccount;
import com.paymybuddy.application.repository.jpa.BankAccountRepositoryJpa;
import com.paymybuddy.application.repository.definition.BankAccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

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
}
