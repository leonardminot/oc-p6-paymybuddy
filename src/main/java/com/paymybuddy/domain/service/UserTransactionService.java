package com.paymybuddy.domain.service;

import com.paymybuddy.domain.dto.UserTransactionCommand;
import com.paymybuddy.domain.model.TransactionModel;
import com.paymybuddy.domain.model.TransferModel;
import com.paymybuddy.domain.repository.BalanceByCurrencyRepository;
import com.paymybuddy.domain.repository.UserTransactionRepository;
import com.paymybuddy.domain.repository.UserTransferRepository;

public class UserTransactionService {
    private final BalanceByCurrencyService balanceByCurrencyService;
    private final UserTransactionRepository userTransactionRepository;
    private final UserTransferRepository userTransferRepository;
    private final BalanceByCurrencyRepository balanceByCurrencyRepository;
    private final DateProvider dateProvider;

    public UserTransactionService(BalanceByCurrencyService balanceByCurrencyService, UserTransactionRepository userTransactionRepository, UserTransferRepository userTransferRepository, BalanceByCurrencyRepository balanceByCurrencyRepository, DateProvider dateProvider) {
        this.balanceByCurrencyService = balanceByCurrencyService;
        this.userTransactionRepository = userTransactionRepository;
        this.userTransferRepository = userTransferRepository;
        this.balanceByCurrencyRepository = balanceByCurrencyRepository;
        this.dateProvider = dateProvider;
    }

    public void performTransaction(UserTransactionCommand userTransactionCommand) {
        // WARNING: To work properly, Spring Data JPA need to save first the transaction in the database and then create the new transfer entity
        // so, the UserTransactionRepository::save need to return the new entity with the completed id.
        throwIfNullFields(userTransactionCommand);
        throwIfAmountIsNegative(userTransactionCommand);

        balanceByCurrencyService.updateBalanceByCurrencyForFromUserOrThrowIfInsufficientAmount(userTransactionCommand);
        balanceByCurrencyService.updateOrCreateToUserBalanceByCurrency(userTransactionCommand);


        TransactionModel transaction = userTransactionRepository.save(new TransactionModel(
                null,
                userTransactionCommand.description(),
                userTransactionCommand.amount(),
                userTransactionCommand.currency(),
                dateProvider.getNow()
        ));


        userTransferRepository.save(new TransferModel(
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
