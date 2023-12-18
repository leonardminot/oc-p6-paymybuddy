package com.paymybuddy.domain.service;

import com.paymybuddy.application.model.Transaction;
import com.paymybuddy.application.model.Transfer;
import com.paymybuddy.domain.dto.UserTransactionCommand;
import com.paymybuddy.domain.repository.UserTransactionRepository;
import com.paymybuddy.domain.repository.UserTransferRepository;

import java.util.UUID;

public class UserTransactionService {
    private final BalanceByCurrencyService balanceByCurrencyService;
    private final UserTransactionRepository userTransactionRepository;
    private final UserTransferRepository userTransferRepository;
    private final DateProvider dateProvider;

    public UserTransactionService(BalanceByCurrencyService balanceByCurrencyService, UserTransactionRepository userTransactionRepository, UserTransferRepository userTransferRepository, DateProvider dateProvider) {
        this.balanceByCurrencyService = balanceByCurrencyService;
        this.userTransactionRepository = userTransactionRepository;
        this.userTransferRepository = userTransferRepository;
        this.dateProvider = dateProvider;
    }

    public void performTransaction(UserTransactionCommand userTransactionCommand) {
        // WARNING: To work properly, Spring Data JPA need to save first the transaction in the database and then create the new transfer entity
        // so, the UserTransactionRepository::save need to return the new entity with the completed id.
        throwIfNullFields(userTransactionCommand);
        throwIfAmountIsNegative(userTransactionCommand);

        balanceByCurrencyService.updateBalanceByCurrencyForFromUserOrThrowIfInsufficientAmount(userTransactionCommand);
        balanceByCurrencyService.updateOrCreateToUserBalanceByCurrency(userTransactionCommand);


        Transaction transaction = userTransactionRepository.save(new Transaction(
                UUID.fromString("00000000-0000-0000-0000-000000000000"),
                userTransactionCommand.description(),
                userTransactionCommand.amount(),
                userTransactionCommand.currency(),
                dateProvider.getNow()
        ));


        userTransferRepository.save(new Transfer(
                userTransactionCommand.fromUser(),
                userTransactionCommand.toUser(),
                transaction
        ));



    }



    private void throwIfAmountIsNegative(UserTransactionCommand userTransactionCommand) {
        if (userTransactionCommand.amount() < 0)
            throw new RuntimeException("Amount must be positive");
    }

    private void throwIfNullFields(UserTransactionCommand userTransactionCommand) {
        if (userTransactionCommand.description() == null)
            throw new RuntimeException("Description must not be null");
        if (userTransactionCommand.currency() == null)
            throw new RuntimeException("Currency must not be null");
        if (userTransactionCommand.fromUser() == null)
            throw new RuntimeException("From user must not be null");
        if (userTransactionCommand.toUser() == null)
            throw new RuntimeException("To user must not be null");
    }
}
