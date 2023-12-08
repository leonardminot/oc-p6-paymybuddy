package com.paymybuddy.utils;

import com.paymybuddy.domain.model.TransferModel;
import com.paymybuddy.domain.repository.UserTransferRepository;

import java.util.ArrayList;
import java.util.List;

public class FakeUserTransferRepository implements UserTransferRepository {
    private final List<TransferModel> transfers = new ArrayList<>();
    @Override
    public void save(TransferModel transfer) {
        transfers.add(transfer);
    }

    @Override
    public List<TransferModel> getAll() {
        return transfers;
    }

    @Override
    public TransferModel get(TransferModel transferModel) {
        return transfers.stream()
                .filter(tra -> tra.equals(transferModel))
                .findAny()
                .orElse(null);
    }
}
