package com.paymybuddy.application.repository.jpa;

import com.paymybuddy.application.model.BankAccount;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface BankAccountRepositoryJpa extends CrudRepository<BankAccount, UUID> {
}
