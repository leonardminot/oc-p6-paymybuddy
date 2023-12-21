package com.paymybuddy.repository.definition;

import com.paymybuddy.model.BankAccount;
import com.paymybuddy.model.BankTransaction;

import java.util.List;

public interface BankTransactionRepository {
    void save(BankTransaction bankTransactionCommand);
    BankTransaction get(BankTransaction bankTransaction);
    List<BankTransaction> getAll();
    List<BankTransaction> getAllFor(BankAccount bankAccount);

}
