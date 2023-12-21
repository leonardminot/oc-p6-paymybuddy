package com.paymybuddy.repository.jpa;

import com.paymybuddy.model.Transfer;
import com.paymybuddy.model.TransferId;
import com.paymybuddy.model.UserAccount;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TransferRepositoryJpa extends CrudRepository<Transfer, TransferId> {
    List<Transfer> findTransferByFromUserOrToUser(UserAccount fromUser, UserAccount toUser);
}
