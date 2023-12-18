package com.paymybuddy.application.repository.jpa;

import com.paymybuddy.application.model.Transfer;
import com.paymybuddy.application.model.TransferId;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface TransferRepositoryJpa extends CrudRepository<Transfer, TransferId> {
}
