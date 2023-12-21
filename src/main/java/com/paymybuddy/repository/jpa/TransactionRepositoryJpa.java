package com.paymybuddy.repository.jpa;

import com.paymybuddy.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface TransactionRepositoryJpa extends JpaRepository<Transaction, UUID> {
}
