package com.paymybuddy.service;

import com.paymybuddy.Exception.BalanceAndTransferException;
import com.paymybuddy.Exception.EmptyFieldException;
import com.paymybuddy.dto.UserTransactionCommand;
import com.paymybuddy.dto.UserTransactionDTO;
import com.paymybuddy.model.Transaction;
import com.paymybuddy.model.Transfer;
import com.paymybuddy.model.UserAccount;
import com.paymybuddy.repository.definition.UserTransactionRepository;
import com.paymybuddy.repository.definition.UserTransferRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.UUID;

@Service
public class UserTransactionService {
    private final BalanceByCurrencyService balanceByCurrencyService;
    private final DeductionService deductionService;
    private final UserTransactionRepository userTransactionRepository;
    private final UserTransferRepository userTransferRepository;
    private final DateProvider dateProvider;

    @Autowired
    public UserTransactionService(BalanceByCurrencyService balanceByCurrencyService, DeductionService deductionService, UserTransactionRepository userTransactionRepository, UserTransferRepository userTransferRepository, DateProvider dateProvider) {
        this.balanceByCurrencyService = balanceByCurrencyService;
        this.deductionService = deductionService;
        this.userTransactionRepository = userTransactionRepository;
        this.userTransferRepository = userTransferRepository;
        this.dateProvider = dateProvider;
    }

    @Transactional
    public void performTransaction(UserTransactionCommand userTransactionCommand) {
        // WARNING: To work properly, Spring Data JPA need to save first the transaction in the database and then create the new transfer entity
        // so, the UserTransactionRepository::save need to return the new entity with the completed id.
        throwIfNullFields(userTransactionCommand);
        throwIfAmountIsNegative(userTransactionCommand);

        double deductionAmount = deductionService.getDeductionAmount(userTransactionCommand.getAmount());

        balanceByCurrencyService.updateBalanceByCurrencyForFromUserOrThrowIfInsufficientAmount(userTransactionCommand);
        balanceByCurrencyService.updateOrCreateToUserBalanceByCurrency(userTransactionCommand, deductionAmount);

        Transaction transaction = userTransactionRepository.save(new Transaction(
                UUID.fromString("00000000-0000-0000-0000-000000000000"),
                userTransactionCommand.getDescription(),
                userTransactionCommand.getAmount() - deductionAmount,
                userTransactionCommand.getCurrency(),
                dateProvider.getNow()
        ));

        deductionService.saveDeductionOfTransaction(transaction);

        userTransferRepository.save(new Transfer(
                userTransactionCommand.getFromUser(),
                userTransactionCommand.getToUser(),
                transaction
        ));
    }

    public List<UserTransactionDTO> getTransactionsFor(UserAccount targetUser) {
        List<Transfer> transfers = userTransferRepository.getAllForUser(targetUser);

        return transfers.stream()
                .map(transfer -> new UserTransactionDTO(
                        transfer.getFromUser(),
                        transfer.getToUser(),
                        transfer.getTransaction().getDescription(),
                        transfer.getFromUser() == targetUser ? -transfer.getTransaction().getAmount() : transfer.getTransaction().getAmount(),
                        transfer.getTransaction().getCurrency(),
                        transfer.getTransaction().getTransactionDate()))
                .sorted(Comparator.comparing(UserTransactionDTO::date).reversed())
                .toList();
    }

    private void throwIfAmountIsNegative(UserTransactionCommand userTransactionCommand) {
        if (userTransactionCommand.getAmount() < 0)
            throw new BalanceAndTransferException("Amount must be positive");
    }

    private void throwIfNullFields(UserTransactionCommand userTransactionCommand) {
        if (userTransactionCommand.getDescription() == null)
            throw new EmptyFieldException("Description must not be null");
        if (userTransactionCommand.getCurrency() == null)
            throw new EmptyFieldException("Currency must not be null");
        if (userTransactionCommand.getFromUser() == null)
            throw new EmptyFieldException("From user must not be null");
        if (userTransactionCommand.getToUser() == null)
            throw new EmptyFieldException("To user must not be null");
    }
}
