package com.paymybuddy.service;

import com.paymybuddy.model.Transaction;
import com.paymybuddy.model.Transfer;
import com.paymybuddy.dto.UserTransactionCommand;
import com.paymybuddy.repository.definition.UserTransactionRepository;
import com.paymybuddy.repository.definition.UserTransferRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class UserTransactionService {
    private final BalanceByCurrencyService balanceByCurrencyService;
    private final UserTransactionRepository userTransactionRepository;
    private final UserTransferRepository userTransferRepository;
    private final DateProvider dateProvider;

    @Autowired
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
