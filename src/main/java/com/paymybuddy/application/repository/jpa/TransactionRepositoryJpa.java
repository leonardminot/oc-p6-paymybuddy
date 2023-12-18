package com.paymybuddy.application.repository.jpa;

import com.paymybuddy.application.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface TransactionRepositoryJpa extends JpaRepository<Transaction, UUID> {
}
