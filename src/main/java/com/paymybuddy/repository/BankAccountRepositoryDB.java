package com.paymybuddy.repository;

import com.paymybuddy.model.BankAccount;
import com.paymybuddy.model.UserAccount;
import com.paymybuddy.repository.definition.BankAccountRepository;
import com.paymybuddy.repository.jpa.BankAccountRepositoryJpa;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public class BankAccountRepositoryDB implements BankAccountRepository {

    private final BankAccountRepositoryJpa bankAccountRepositoryJpa;

    @Autowired
    public BankAccountRepositoryDB(BankAccountRepositoryJpa bankAccountRepositoryJpa) {
        this.bankAccountRepositoryJpa = bankAccountRepositoryJpa;
    }

    @Override
    public BankAccount save(BankAccount bankAccount) {
        return bankAccountRepositoryJpa.save(bankAccount);
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
        return bankAccountRepositoryJpa.findBankAccountsByUserAccountEquals(user);
    }

    @Override
    public Optional<BankAccount> getById(UUID bankAccountId) {
        return bankAccountRepositoryJpa.findById(bankAccountId);
    }
}
