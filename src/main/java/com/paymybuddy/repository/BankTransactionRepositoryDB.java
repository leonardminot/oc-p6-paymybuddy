package com.paymybuddy.repository;

import com.paymybuddy.model.BankAccount;
import com.paymybuddy.model.BankTransaction;
import com.paymybuddy.repository.jpa.BankTransactionRepositoryJpa;
import com.paymybuddy.repository.definition.BankTransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.StreamSupport;

@Repository
public class BankTransactionRepositoryDB implements BankTransactionRepository {

    private final BankTransactionRepositoryJpa bankTransactionRepositoryJpa;

    @Autowired
    public BankTransactionRepositoryDB(BankTransactionRepositoryJpa bankTransactionRepositoryJpa) {
        this.bankTransactionRepositoryJpa = bankTransactionRepositoryJpa;
    }

    @Override
    public void save(BankTransaction bankTransactionCommand) {
        bankTransactionRepositoryJpa.save(bankTransactionCommand);
    }

    @Override
    public BankTransaction get(BankTransaction bankTransaction) {
        return bankTransactionRepositoryJpa.findById(bankTransaction.getBankTransactionID()).orElse(null);
    }

    @Override
    public List<BankTransaction> getAll() {
        Iterable<BankTransaction> allBankTransactions = bankTransactionRepositoryJpa.findAll();
        return StreamSupport.stream(allBankTransactions.spliterator(), false).toList();
    }

    @Override
    public List<BankTransaction> getAllFor(BankAccount bankAccount) {
        return bankTransactionRepositoryJpa.findBankTransactionsByBankAccountEquals(bankAccount);
    }
}
