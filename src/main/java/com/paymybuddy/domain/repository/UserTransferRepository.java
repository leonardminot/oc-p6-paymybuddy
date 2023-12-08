package com.paymybuddy.domain.repository;

import com.paymybuddy.domain.model.TransferModel;

import java.util.List;

public interface UserTransferRepository {
    void save(TransferModel transfer);
    List<TransferModel> getAll();
    TransferModel get(TransferModel transferModel);
}
