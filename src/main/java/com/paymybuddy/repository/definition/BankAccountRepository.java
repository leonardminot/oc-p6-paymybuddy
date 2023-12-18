package com.paymybuddy.repository.definition;

import com.paymybuddy.model.BankAccount;

public interface BankAccountRepository {
    void save(BankAccount bankAccount);
    BankAccount get(BankAccount bankAccount);
    boolean isIBANExists(String iban);
}
