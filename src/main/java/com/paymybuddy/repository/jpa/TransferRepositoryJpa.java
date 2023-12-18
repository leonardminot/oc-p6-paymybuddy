package com.paymybuddy.repository.jpa;

import com.paymybuddy.model.Transfer;
import com.paymybuddy.model.TransferId;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TransferRepositoryJpa extends CrudRepository<Transfer, TransferId> {
}
