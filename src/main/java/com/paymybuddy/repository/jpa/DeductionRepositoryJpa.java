package com.paymybuddy.repository.jpa;

import com.paymybuddy.model.PayMyBuddyDeduction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface DeductionRepositoryJpa extends JpaRepository<PayMyBuddyDeduction, UUID> {
}
