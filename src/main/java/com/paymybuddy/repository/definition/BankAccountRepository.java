package com.paymybuddy.repository.definition;

import com.paymybuddy.model.BankAccount;
import com.paymybuddy.model.UserAccount;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface BankAccountRepository {
    BankAccount save(BankAccount bankAccount);
    BankAccount get(BankAccount bankAccount);
    boolean isIBANExists(String iban);

    List<BankAccount> fetchAllBankAccountsForUser(UserAccount user);

    Optional<BankAccount> getById(UUID bankAccountId);
}
