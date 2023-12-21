package com.paymybuddy.repository.definition;

import com.paymybuddy.model.Transfer;
import com.paymybuddy.model.UserAccount;

import java.util.List;

public interface UserTransferRepository {
    void save(Transfer transfer);
    List<Transfer> getAll();
    Transfer get(Transfer transferModel);
    List<Transfer> getAllForUser(UserAccount user);
}
