package com.paymybuddy.utils;

import com.paymybuddy.model.Transfer;
import com.paymybuddy.repository.definition.UserTransferRepository;

import java.util.ArrayList;
import java.util.List;

public class FakeUserTransferRepository implements UserTransferRepository {
    private final List<Transfer> transfers = new ArrayList<>();
    @Override
    public void save(Transfer transfer) {
        transfers.add(transfer);
    }

    @Override
    public List<Transfer> getAll() {
        return transfers;
    }

    @Override
    public Transfer get(Transfer transferModel) {
        return transfers.stream()
                .filter(tra -> tra.equals(transferModel))
                .findAny()
                .orElse(null);
    }
}
