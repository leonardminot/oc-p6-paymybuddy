package com.paymybuddy.domain.service;

import com.paymybuddy.domain.dto.UserTransactionCommand;
import com.paymybuddy.domain.model.BalanceByCurrencyModel;
import com.paymybuddy.domain.model.TransactionModel;
import com.paymybuddy.domain.model.TransferModel;
import com.paymybuddy.domain.repository.BalanceByCurrencyRepository;
import com.paymybuddy.domain.repository.UserTransactionRepository;
import com.paymybuddy.domain.repository.UserTransferRepository;

import java.util.Optional;

public class UserTransactionService {
    private final UserTransactionRepository userTransactionRepository;
    private final UserTransferRepository userTransferRepository;
    private final BalanceByCurrencyRepository balanceByCurrencyRepository;
    private final DateProvider dateProvider;

    public UserTransactionService(UserTransactionRepository userTransactionRepository, UserTransferRepository userTransferRepository, BalanceByCurrencyRepository balanceByCurrencyRepository, DateProvider dateProvider) {
        this.userTransactionRepository = userTransactionRepository;
        this.userTransferRepository = userTransferRepository;
        this.balanceByCurrencyRepository = balanceByCurrencyRepository;
        this.dateProvider = dateProvider;
    }

    public void performTransaction(UserTransactionCommand userTransactionCommand) {
        //TODO: Refactoring de la méthode
        //   A vérifier : redondance avec Bank Transaction a supprimer

        // WARNING: To work properly, Spring Data JPA need to save first the transaction in the database and then create the new transfer entity
        // so, the UserTransactionRepository::save need to return the new entity with the completed id.
        throwIfNullFields(userTransactionCommand);
        throwIfAmountIsNegative(userTransactionCommand);

        Optional<BalanceByCurrencyModel> fromUserBalanceByCurrency = balanceByCurrencyRepository.getByUserAccountAndCurrency(
                userTransactionCommand.fromUser(),
                userTransactionCommand.currency()
        );

        if (fromUserBalanceByCurrency.isPresent()) {
            BalanceByCurrencyModel currentBalanceByCurrency = fromUserBalanceByCurrency.get();

            if (currentBalanceByCurrency.balance() < userTransactionCommand.amount()) {
                throw new RuntimeException("Insufficient balance for From User");
            }

            balanceByCurrencyRepository.save(new BalanceByCurrencyModel(
                    currentBalanceByCurrency.id(),
                    currentBalanceByCurrency.userAccount(),
                    currentBalanceByCurrency.balance() - userTransactionCommand.amount(),
                    currentBalanceByCurrency.currency()
            ));
        } else {
            throw new RuntimeException("No BalanceByCurrency for From User found");
        }

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

        Optional<BalanceByCurrencyModel> toUserBalanceByCurrency = balanceByCurrencyRepository.getByUserAccountAndCurrency(
                userTransactionCommand.toUser(),
                userTransactionCommand.currency()
        );

        if (toUserBalanceByCurrency.isPresent()) {
            BalanceByCurrencyModel balanceByCurrencyModel = toUserBalanceByCurrency.get();

            balanceByCurrencyRepository.save(new BalanceByCurrencyModel(
                    balanceByCurrencyModel.id(),
                    balanceByCurrencyModel.userAccount(),
                    balanceByCurrencyModel.balance() + userTransactionCommand.amount(),
                    balanceByCurrencyModel.currency()
            ));

        } else {
            balanceByCurrencyRepository.save(new BalanceByCurrencyModel(
                    null,
                    userTransactionCommand.toUser(),
                    userTransactionCommand.amount(),
                    userTransactionCommand.currency()
            ));
        }


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
