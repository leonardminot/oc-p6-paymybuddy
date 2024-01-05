package com.paymybuddy.integration;

import com.paymybuddy.model.*;
import com.paymybuddy.repository.jpa.*;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
@Slf4j
public class RepositoryJPAIT {

    @Autowired
    private UserAccountRepositoryJpa userAccountRepositoryJpa;
    @Autowired
    private RelationRepositoryJpa relationRepositoryJpa;
    @Autowired
    private BankAccountRepositoryJpa bankAccountRepositoryJpa;
    @Autowired
    private BalanceByCurrencyRepositoryJpa balanceByCurrencyRepositoryJpa;
    @Autowired
    private BankTransactionRepositoryJpa bankTransactionRepositoryJpa;
    @Autowired
    private TransactionRepositoryJpa transactionRepositoryJpa;
    @Autowired
    private TransferRepositoryJpa transferRepositoryJpa;

    @BeforeEach
    void setUp() {
        userAccountRepositoryJpa.deleteAll();
        relationRepositoryJpa.deleteAll();
        bankAccountRepositoryJpa.deleteAll();
        balanceByCurrencyRepositoryJpa.deleteAll();
        bankTransactionRepositoryJpa.deleteAll();
        transactionRepositoryJpa.deleteAll();
        transferRepositoryJpa.deleteAll();
    }

    @Test
    void itShouldSaveANewUserAccount() {
        // Given
        UserAccount userToSave = new UserAccount(
                "Léo",
                "Minot",
                "leo@email.com",
                "Welcome123",
                "leoM",
                "USER"
        );

        // When
        userAccountRepositoryJpa.save(userToSave);

        // Then
        Iterable<UserAccount> userAccounts = userAccountRepositoryJpa.findAll();
        assertThat(userToSave).isIn(userAccounts);
    }

    @Test
    void itShouldCreateARelationBetweenTwoUsers() {
        // Given
        UserAccount user1 = new UserAccount(
                "Léo",
                "Minot",
                "leo@email.com",
                "Welcome123",
                "leoM",
                "USER"
        );
        userAccountRepositoryJpa.save(user1);

        UserAccount user2 = new UserAccount(
                "Victor",
                "Minot",
                "victor@email.com",
                "Welcome123",
                "VicM",
                "USER"
        );
        userAccountRepositoryJpa.save(user2);

        // When
        Relation relation = new Relation(user1, user2, LocalDateTime.now());
        relationRepositoryJpa.save(relation);

        // Then
        Iterable<Relation> relations = relationRepositoryJpa.findAll();
        assertThat(relation).isIn(relations);
        System.out.println(relation);
    }

    @Test
    void itShouldCreateABankAccount() {
        // Given
        UserAccount user1 = new UserAccount(
                "Léo",
                "Minot",
                "leo@email.com",
                "Welcome123",
                "leoM",
                "USER"
        );
        userAccountRepositoryJpa.save(user1);

        // When
        BankAccount bankAccount1 = new BankAccount(
                "123456789",
                "FR",
                user1
        );
        bankAccountRepositoryJpa.save(bankAccount1);

        BankAccount bankAccount2 = new BankAccount(
                "987654321",
                "FR",
                user1
        );
        bankAccountRepositoryJpa.save(bankAccount2);

        // Then
        Iterable<BankAccount> bankAccounts = bankAccountRepositoryJpa.findAll();
        assertThat(bankAccount1).isIn(bankAccounts);
        assertThat(bankAccount2).isIn(bankAccounts);
    }

    @Test
    void itShouldCreateABalanceByCurrency() {
        // Given
        UserAccount user1 = new UserAccount(
                "Léo",
                "Minot",
                "leo@email.com",
                "Welcome123",
                "leoM",
                "USER"
        );
        userAccountRepositoryJpa.save(user1);

        // When
        BalanceByCurrency balanceByCurrency = new BalanceByCurrency(
                100.0,
                Currency.EUR,
                user1
        );
        balanceByCurrencyRepositoryJpa.save(balanceByCurrency);

        // Then
        Iterable<BalanceByCurrency> balanceByCurrencies = balanceByCurrencyRepositoryJpa.findAll();
        assertThat(balanceByCurrency).isIn(balanceByCurrencies);
    }

    @Test
    void itShouldCreateABankTransaction() {
        // Given
        UserAccount user1 = new UserAccount(
                "Léo",
                "Minot",
                "leo@email.com",
                "Welcome123",
                "leoM",
                "USER"
        );
        userAccountRepositoryJpa.save(user1);

        BankAccount bankAccount1 = new BankAccount(
                "123456789",
                "FR",
                user1
        );
        bankAccountRepositoryJpa.save(bankAccount1);

        // When
        BankTransaction bankTransaction = new BankTransaction(
                100.0,
                Currency.EUR,
                LocalDateTime.now(),
                bankAccount1
        );
        bankTransactionRepositoryJpa.save(bankTransaction);

        // Then
        Iterable<BankTransaction> bankTransactions = bankTransactionRepositoryJpa.findAll();
        assertThat(bankTransaction).isIn(bankTransactions);
    }

    @Test
    void itShouldShouldCreateATransferBetweenUsers() {
        // Given
        UserAccount user1 = new UserAccount(
                "Léo",
                "Minot",
                "leo@email.com",
                "Welcome123",
                "leoM",
                "USER"
        );
        userAccountRepositoryJpa.save(user1);

        UserAccount user2 = new UserAccount(
                "Victor",
                "Minot",
                "victor@email.com",
                "Welcome123",
                "VicM",
                "USER"
        );
        userAccountRepositoryJpa.save(user2);

        // When
        Transaction transaction = new Transaction(
                "nom transaction",
                100.0,
                Currency.EUR,
                LocalDateTime.now()
        );

        transaction = transactionRepositoryJpa.saveAndFlush(transaction);
        log.info("Transaction : " + transaction);

        Transfer transfer = new Transfer(
                user1,
                user2,
                transaction
        );

        transferRepositoryJpa.save(transfer);

        // Then
        Iterable<Transaction> transactions = transactionRepositoryJpa.findAll();
        assertThat(transaction).isIn(transactions);

        Iterable<Transfer> transfers = transferRepositoryJpa.findAll();
        assertThat(transfer).isIn(transfers);
    }
}
