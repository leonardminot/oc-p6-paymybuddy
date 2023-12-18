package com.paymybuddy.domain.repository;

import com.paymybuddy.application.model.BankAccount;

public interface BankAccountRepository {
    void save(BankAccount bankAccount);
    BankAccount get(BankAccount bankAccount);
    boolean isIBANExists(String iban);
}
