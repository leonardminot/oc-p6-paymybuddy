package com.paymybuddy.application.repository.definition;

import com.paymybuddy.application.model.Transfer;

import java.util.List;

public interface UserTransferRepository {
    void save(Transfer transfer);
    List<Transfer> getAll();
    Transfer get(Transfer transferModel);
}