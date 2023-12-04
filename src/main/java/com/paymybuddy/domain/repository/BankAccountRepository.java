package com.paymybuddy.domain.repository;

import com.paymybuddy.domain.model.BankAccountModel;

public interface BankAccountRepository {
    void save(BankAccountModel bankAccount);
    BankAccountModel get(BankAccountModel bankAccount);
    boolean isIBANExists(String iban);
}
