package com.paymybuddy.application.repository;

import com.paymybuddy.application.model.Transfer;
import com.paymybuddy.application.repository.jpa.TransferRepositoryJpa;
import com.paymybuddy.application.repository.definition.UserTransferRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.StreamSupport;

@Repository
public class TransferRepositoryDB implements UserTransferRepository {
    private final TransferRepositoryJpa transferRepositoryJpa;

    @Autowired
    public TransferRepositoryDB(TransferRepositoryJpa transferRepositoryJpa) {
        this.transferRepositoryJpa = transferRepositoryJpa;
    }

    @Override
    public void save(Transfer transfer) {
        transferRepositoryJpa.save(transfer);
    }

    @Override
    public List<Transfer> getAll() {
        Iterable<Transfer> transfers = transferRepositoryJpa.findAll();
        return StreamSupport.stream(transfers.spliterator(), false).toList();
    }

    @Override
    public Transfer get(Transfer transferModel) {
        return transferRepositoryJpa.findById(transferModel.getTransferId()).orElse(null);
    }
}
