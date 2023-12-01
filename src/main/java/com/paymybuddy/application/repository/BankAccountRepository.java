package com.paymybuddy.application.repository;

import com.paymybuddy.application.model.BankAccount;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface BankAccountRepository extends CrudRepository<BankAccount, UUID> {
}
