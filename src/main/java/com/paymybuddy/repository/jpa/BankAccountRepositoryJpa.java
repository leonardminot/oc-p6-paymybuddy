package com.paymybuddy.repository.jpa;

import com.paymybuddy.model.BankAccount;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface BankAccountRepositoryJpa extends CrudRepository<BankAccount, UUID> {
    Optional<BankAccount> findByIban(String iban);
}
