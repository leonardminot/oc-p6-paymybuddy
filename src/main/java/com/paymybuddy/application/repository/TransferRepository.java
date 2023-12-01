package com.paymybuddy.application.repository;

import com.paymybuddy.application.model.Transfer;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface TransferRepository extends CrudRepository<Transfer, UUID> {
}
