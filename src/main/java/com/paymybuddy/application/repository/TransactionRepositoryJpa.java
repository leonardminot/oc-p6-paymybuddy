package com.paymybuddy.application.repository;

import com.paymybuddy.application.model.Transaction;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface TransactionRepositoryJpa extends CrudRepository<Transaction, UUID> {
}
