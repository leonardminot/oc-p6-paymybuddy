package com.paymybuddy.repository.definition;

import com.paymybuddy.model.BankAccount;
import com.paymybuddy.model.UserAccount;

import java.util.List;

public interface BankAccountRepository {
    void save(BankAccount bankAccount);
    BankAccount get(BankAccount bankAccount);
    boolean isIBANExists(String iban);

    List<BankAccount> fetchAllBankAccountsForUser(UserAccount user);
}
