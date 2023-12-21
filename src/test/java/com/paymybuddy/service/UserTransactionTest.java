package com.paymybuddy.service;

import com.paymybuddy.dto.UserTransactionDTO;
import com.paymybuddy.model.BalanceByCurrency;
import com.paymybuddy.model.Transaction;
import com.paymybuddy.model.Transfer;
import com.paymybuddy.model.UserAccount;
import com.paymybuddy.utils.Fixture;
import com.paymybuddy.utils.UserAccountBuilder;
import org.junit.jupiter.api.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Tag("UnitTest")
public class UserTransactionTest {
    @Nested
    @DisplayName("Feature: create a transaction between users")
    class CreateTransactionBetweenUsers {
        private Fixture fixture;

        @BeforeEach
        void setUp() {
            fixture = new Fixture();
        }

        @Nested
        @DisplayName("Rule: no fields should be null")
        class NoFieldsShouldBeBlank {
            @Test
            void itShouldCreateANewTransaction() {
                // Given
                UserAccount fromUser = new UserAccountBuilder()
                        .withId(UUID.fromString("1124d9e8-6266-4bcf-8035-37a02ba75c69"))
                        .withFirstName("Léo")
                        .withLastName("Minot")
                        .build();

                fixture.givenUserInDatabase(fromUser);

                LocalDateTime now = LocalDateTime.of(2013, 12, 1, 15, 42, 0, 0);
                fixture.givenNowIs(now);


                UserAccount toUser = new UserAccountBuilder()
                        .withId(UUID.fromString("22222222-6266-4bcf-8035-37a02ba75c69"))
                        .withFirstName("Victor")
                        .withLastName("Minot")
                        .build();

                fixture.givenUserInDatabase(toUser);

                fixture.givenTheBalanceByCurrencyInDataBase(new BalanceByCurrency(UUID.fromString("44444444-6266-4bcf-8035-37a02ba75c69"), fromUser, 100.0, "EUR"));


                // When
                fixture.whenCreateATransactionBetweenUsers(fromUser, toUser, "test transaction", "EUR", 100.0);

                // Then
                fixture.thenItShouldCreateATransactionOf(new Transaction(UUID.fromString("00000000-0000-0000-0000-000000000000"), "test transaction", 100.0, "EUR", now));
            }

            @Test
            void itShouldThrowIfDescriptionIsNull() {
                // Given
                UserAccount fromUser = new UserAccountBuilder()
                        .withId(UUID.fromString("1124d9e8-6266-4bcf-8035-37a02ba75c69"))
                        .withFirstName("Léo")
                        .withLastName("Minot")
                        .build();

                fixture.givenUserInDatabase(fromUser);

                LocalDateTime now = LocalDateTime.of(2013, 12, 1, 15, 42, 0, 0);

                UserAccount toUser = new UserAccountBuilder()
                        .withId(UUID.fromString("22222222-6266-4bcf-8035-37a02ba75c69"))
                        .withFirstName("Victor")
                        .withLastName("Minot")
                        .build();

                fixture.givenUserInDatabase(toUser);

                fixture.givenNowIs(now);

                // When
                // Then
                fixture.whenCreateATransactionBetweenUsersAndThenThrow(fromUser, toUser, null, "EUR", 100.0, new RuntimeException("Description must not be null"));
            }

            @Test
            void itShouldThrowIfCurrencyIsNull() {
                // Given
                UserAccount fromUser = new UserAccountBuilder()
                        .withId(UUID.fromString("1124d9e8-6266-4bcf-8035-37a02ba75c69"))
                        .withFirstName("Léo")
                        .withLastName("Minot")
                        .build();

                fixture.givenUserInDatabase(fromUser);

                LocalDateTime now = LocalDateTime.of(2013, 12, 1, 15, 42, 0, 0);

                UserAccount toUser = new UserAccountBuilder()
                        .withId(UUID.fromString("22222222-6266-4bcf-8035-37a02ba75c69"))
                        .withFirstName("Victor")
                        .withLastName("Minot")
                        .build();

                fixture.givenUserInDatabase(toUser);

                fixture.givenNowIs(now);

                // When
                // Then
                fixture.whenCreateATransactionBetweenUsersAndThenThrow(fromUser, toUser, "test transaction", null, 100.0, new RuntimeException("Currency must not be null"));
            }

            @Test
            void itShouldThrowIfFromUserIsNull() {
                // Given
                LocalDateTime now = LocalDateTime.of(2013, 12, 1, 15, 42, 0, 0);

                UserAccount toUser = new UserAccountBuilder()
                        .withId(UUID.fromString("22222222-6266-4bcf-8035-37a02ba75c69"))
                        .withFirstName("Victor")
                        .withLastName("Minot")
                        .build();

                fixture.givenUserInDatabase(toUser);

                fixture.givenNowIs(now);

                // When
                // Then
                fixture.whenCreateATransactionBetweenUsersAndThenThrow(null, toUser, "test transaction", "EUR", 100.0, new RuntimeException("From user must not be null"));
            }

            @Test
            void itShouldThrowIfToUserIsNull() {
                UserAccount fromUser = new UserAccountBuilder()
                        .withId(UUID.fromString("1124d9e8-6266-4bcf-8035-37a02ba75c69"))
                        .withFirstName("Léo")
                        .withLastName("Minot")
                        .build();

                fixture.givenUserInDatabase(fromUser);

                LocalDateTime now = LocalDateTime.of(2013, 12, 1, 15, 42, 0, 0);

                fixture.givenNowIs(now);

                fixture.whenCreateATransactionBetweenUsersAndThenThrow(fromUser, null, "test transaction", "EUR", 100.0, new RuntimeException("To user must not be null"));
            }

            @Test
            void itShouldThrowIfAmountIsNegative() {
                UserAccount fromUser = new UserAccountBuilder()
                        .withId(UUID.fromString("1124d9e8-6266-4bcf-8035-37a02ba75c69"))
                        .withFirstName("Léo")
                        .withLastName("Minot")
                        .build();

                fixture.givenUserInDatabase(fromUser);

                LocalDateTime now = LocalDateTime.of(2013, 12, 1, 15, 42, 0, 0);
                fixture.givenNowIs(now);

                UserAccount toUser = new UserAccountBuilder()
                        .withId(UUID.fromString("22222222-6266-4bcf-8035-37a02ba75c69"))
                        .withFirstName("Victor")
                        .withLastName("Minot")
                        .build();

                fixture.givenUserInDatabase(toUser);

                fixture.whenCreateATransactionBetweenUsersAndThenThrow(fromUser, toUser, "test transaction", "EUR", -100.0, new RuntimeException("Amount must be positive"));
            }
        }

        @Nested
        @DisplayName("Rule: create a transfer when creating a transaction")
        class CreateATransferBetweenUsers {
            @Test
            void itShouldCreateATransferBetweenUser() {
                // Given
                UserAccount fromUser = new UserAccountBuilder()
                        .withId(UUID.fromString("1124d9e8-6266-4bcf-8035-37a02ba75c69"))
                        .withFirstName("Léo")
                        .withLastName("Minot")
                        .build();

                fixture.givenUserInDatabase(fromUser);

                LocalDateTime now = LocalDateTime.of(2013, 12, 1, 15, 42, 0, 0);
                fixture.givenNowIs(now);


                UserAccount toUser = new UserAccountBuilder()
                        .withId(UUID.fromString("22222222-6266-4bcf-8035-37a02ba75c69"))
                        .withFirstName("Victor")
                        .withLastName("Minot")
                        .build();

                fixture.givenUserInDatabase(toUser);

                fixture.givenTheBalanceByCurrencyInDataBase(new BalanceByCurrency(UUID.fromString("44444444-6266-4bcf-8035-37a02ba75c69"), fromUser, 100.0, "EUR"));

                // When
                fixture.whenCreateATransactionBetweenUsers(fromUser, toUser, "test transaction", "EUR", 100.0);

                // Then
                fixture.thenItShouldCreateATransferOf(new Transfer(fromUser, toUser, new Transaction(UUID.fromString("00000000-0000-0000-0000-000000000000"), "test transaction", 100.0, "EUR", now)));
            }
        }
    }

    @Nested
    @DisplayName("Feature: modify the balance by currency when performing a user transaction")
    class ModifyBalanceByCurrency {

        @Nested
        @DisplayName("Rule: it should create or update a balance by currency for the toUser if not exists")
        class CreateABalanceByCurrencyForTheToUser {
            private Fixture fixture;

            @BeforeEach
            void setUp() {
                fixture = new Fixture();
            }
            @Test
            void itShouldCreateABalanceByCurrencyForToUser() {
                // Given
                UserAccount fromUser = new UserAccountBuilder()
                        .withId(UUID.fromString("1124d9e8-6266-4bcf-8035-37a02ba75c69"))
                        .withFirstName("Léo")
                        .withLastName("Minot")
                        .build();

                fixture.givenUserInDatabase(fromUser);



                UserAccount toUser = new UserAccountBuilder()
                        .withId(UUID.fromString("22222222-6266-4bcf-8035-37a02ba75c69"))
                        .withFirstName("Victor")
                        .withLastName("Minot")
                        .build();

                fixture.givenUserInDatabase(toUser);

                LocalDateTime now = LocalDateTime.of(2013, 12, 1, 15, 42, 0, 0);
                fixture.givenNowIs(now);

                fixture.givenTheBalanceByCurrencyInDataBase(new BalanceByCurrency(UUID.fromString("44444444-6266-4bcf-8035-37a02ba75c69"), fromUser, 100.0, "USD"));
                // When
                fixture.whenCreateATransactionBetweenUsers(fromUser, toUser, "Transaction description", "USD", 100.0);

                // Then
                fixture.thenBalanceByCurrencyShouldBe(new BalanceByCurrency(
                        UUID.fromString("00000000-0000-0000-0000-000000000000"),
                        toUser,
                        100.0,
                        "USD"
                ));

            }

            @Test
            void itShouldUpdateABalanceByCurrencyForTheToUser() {
                // Given
                UserAccount fromUser = new UserAccountBuilder()
                        .withId(UUID.fromString("1124d9e8-6266-4bcf-8035-37a02ba75c69"))
                        .withFirstName("Léo")
                        .withLastName("Minot")
                        .build();

                fixture.givenUserInDatabase(fromUser);

                UserAccount toUser = new UserAccountBuilder()
                        .withId(UUID.fromString("22222222-6266-4bcf-8035-37a02ba75c69"))
                        .withFirstName("Victor")
                        .withLastName("Minot")
                        .build();

                fixture.givenUserInDatabase(toUser);

                LocalDateTime now = LocalDateTime.of(2013, 12, 1, 15, 42, 0, 0);
                fixture.givenNowIs(now);

                BalanceByCurrency toUserBalanceByCurrency = new BalanceByCurrency(
                        UUID.fromString("33333333-6266-4bcf-8035-37a02ba75c69"),
                        toUser,
                        50.0,
                        "USD"
                );
                fixture.givenTheBalanceByCurrencyInDataBase(toUserBalanceByCurrency);

                BalanceByCurrency fromUserBalanceByCurrency = new BalanceByCurrency(UUID.fromString("44444444-6266-4bcf-8035-37a02ba75c69"), fromUser, 100.0, "USD");
                fixture.givenTheBalanceByCurrencyInDataBase(fromUserBalanceByCurrency);


                // When
                fixture.whenCreateATransactionBetweenUsers(fromUser, toUser, "Transaction description", "USD", 100.0);

                // Then
                fixture.thenBalanceByCurrencyShouldBe(new BalanceByCurrency(
                        toUserBalanceByCurrency.getBalanceID(),
                        toUser,
                        150.0,
                        "USD"
                ));
                fixture.thenBalanceByCurrencyShouldHaveLengthOf(2);
            }
        }

        @Nested
        @DisplayName("Rule: it should update From User BalanceByCurrency")
        class FromUserBalanceByCurrencyManagement {
            private Fixture fixture;

            @BeforeEach
            void setUp() {
                fixture = new Fixture();
            }
            @Test
            void itShouldUpdateFromUserBalanceByCurrency() {
                // Given
                UserAccount fromUser = new UserAccountBuilder()
                        .withId(UUID.fromString("1124d9e8-6266-4bcf-8035-37a02ba75c69"))
                        .withFirstName("Léo")
                        .withLastName("Minot")
                        .build();

                fixture.givenUserInDatabase(fromUser);

                UserAccount toUser = new UserAccountBuilder()
                        .withId(UUID.fromString("22222222-6266-4bcf-8035-37a02ba75c69"))
                        .withFirstName("Victor")
                        .withLastName("Minot")
                        .build();

                fixture.givenUserInDatabase(toUser);

                LocalDateTime now = LocalDateTime.of(2013, 12, 1, 15, 42, 0, 0);
                fixture.givenNowIs(now);

                BalanceByCurrency fromUserBalanceByCurrency = new BalanceByCurrency(
                        UUID.fromString("44444444-6266-4bcf-8035-37a02ba75c69"),
                        fromUser,
                        50.0,
                        "USD"
                );
                fixture.givenTheBalanceByCurrencyInDataBase(fromUserBalanceByCurrency);

                // When
                fixture.whenCreateATransactionBetweenUsers(fromUser, toUser, "test transaction", "USD", 30.0);

                // Then
                fixture.thenBalanceByCurrencyShouldBeWithAmountVerification(new BalanceByCurrency(
                        UUID.fromString("44444444-6266-4bcf-8035-37a02ba75c69"),
                        fromUser,
                        20.0,
                        "USD"
                ));

            }

            @Test
            void itShouldThrowIfFromUserBalanceByCurrencyIsUnknown() {
                // Given
                UserAccount fromUser = new UserAccountBuilder()
                        .withId(UUID.fromString("1124d9e8-6266-4bcf-8035-37a02ba75c69"))
                        .withFirstName("Léo")
                        .withLastName("Minot")
                        .build();

                fixture.givenUserInDatabase(fromUser);

                UserAccount toUser = new UserAccountBuilder()
                        .withId(UUID.fromString("22222222-6266-4bcf-8035-37a02ba75c69"))
                        .withFirstName("Victor")
                        .withLastName("Minot")
                        .build();

                fixture.givenUserInDatabase(toUser);

                LocalDateTime now = LocalDateTime.of(2013, 12, 1, 15, 42, 0, 0);
                fixture.givenNowIs(now);

                // When
                // Then
                fixture.whenCreateATransactionBetweenUsersAndThenThrow(fromUser, toUser, "test transaction", "USD", 100.0, new RuntimeException("No BalanceByCurrency for From User found"));
            }

            @Test
            void itShouldThrowIfFromUserBalanceByCurrencyBecameNegative() {
                // Given
                UserAccount fromUser = new UserAccountBuilder()
                        .withId(UUID.fromString("1124d9e8-6266-4bcf-8035-37a02ba75c69"))
                        .withFirstName("Léo")
                        .withLastName("Minot")
                        .build();

                fixture.givenUserInDatabase(fromUser);

                UserAccount toUser = new UserAccountBuilder()
                        .withId(UUID.fromString("22222222-6266-4bcf-8035-37a02ba75c69"))
                        .withFirstName("Victor")
                        .withLastName("Minot")
                        .build();

                fixture.givenUserInDatabase(toUser);

                LocalDateTime now = LocalDateTime.of(2013, 12, 1, 15, 42, 0, 0);
                fixture.givenNowIs(now);

                BalanceByCurrency fromUserBalanceByCurrency = new BalanceByCurrency(
                        UUID.fromString("44444444-6266-4bcf-8035-37a02ba75c69"),
                        fromUser,
                        50.0,
                        "USD"
                );
                fixture.givenTheBalanceByCurrencyInDataBase(fromUserBalanceByCurrency);

                // When
                // Then
                fixture.whenCreateATransactionBetweenUsersAndThenThrow(fromUser, toUser, "test transaction", "USD", 100.0, new RuntimeException("Amount can not go beyond 0"));
            }
        }
    }

    @Nested
    @DisplayName("Feature: fetch user transactions")
    class FetchAllUserTransactions {

        @Nested
        @DisplayName("Rule: fetch user transactions for a given user")
        class FetchUserTransactionsForAGivenUser {
            private Fixture fixture;

            @BeforeEach
            void setUp() {
                fixture = new Fixture();
            }

            @Test
            void itShouldFetchUserTransaction() {
                // Given
                UserAccount targetUser = new UserAccountBuilder()
                        .withId(UUID.fromString("1124d9e8-6266-4bcf-8035-37a02ba75c69"))
                        .withFirstName("Léo")
                        .withLastName("Minot")
                        .build();

                fixture.givenUserInDatabase(targetUser);

                UserAccount otherUser = new UserAccountBuilder()
                        .withId(UUID.fromString("22222222-6266-4bcf-8035-37a02ba75c69"))
                        .withFirstName("Victor")
                        .withLastName("Minot")
                        .build();

                fixture.givenUserInDatabase(otherUser);

                UserAccount thirdUser = new UserAccountBuilder()
                        .withId(UUID.fromString("33333333-6266-4bcf-8035-37a02ba75c69"))
                        .withFirstName("Victor")
                        .withLastName("Minot")
                        .build();

                Transaction transaction1 = new Transaction(
                        UUID.fromString("00000000-6266-4bcf-8035-37a02ba75c69"),
                        "Première transaction",
                        100.0,
                        "EUR",
                        LocalDateTime.of(2023, 12, 21, 15, 0,0)
                );

                Transaction transaction2 = new Transaction(
                        UUID.fromString("00000001-6266-4bcf-8035-37a02ba75c69"),
                        "Deuxième transaction",
                        100.0,
                        "EUR",
                        LocalDateTime.of(2023, 12, 22, 15, 0,0)
                );

                Transaction transaction3 = new Transaction(
                        UUID.fromString("00000000-6266-4bcf-8035-37a02ba75c69"),
                        "Troisième transaction",
                        100.0,
                        "EUR",
                        LocalDateTime.of(2023, 12, 23, 15, 0,0)
                );

                Transfer transfer1 = new Transfer(
                        targetUser,
                        otherUser,
                        transaction1
                );

                Transfer transfer2 = new Transfer(
                        thirdUser,
                        targetUser,
                        transaction2
                );

                Transfer transfer3 = new Transfer(
                        otherUser,
                        thirdUser,
                        transaction3
                );

                fixture.givenTheUserTransactionInDatabase(transaction1);
                fixture.givenTheUserTransactionInDatabase(transaction2);
                fixture.givenTheUserTransactionInDatabase(transaction3);

                fixture.givenTheUserTransferInDatabase(transfer1);
                fixture.givenTheUserTransferInDatabase(transfer2);
                fixture.givenTheUserTransferInDatabase(transfer3);

                // When
                fixture.whenFetchUserTransactionFor(targetUser);

                // Then
                fixture.thenTransactionListShouldBe(List.of(
                        new UserTransactionDTO(targetUser, otherUser, "Première transaction", -100.0, "EUR", LocalDateTime.of(2023, 12, 21, 15, 0,0)),
                        new UserTransactionDTO(thirdUser, targetUser, "Deuxième transaction", 100.0, "EUR", LocalDateTime.of(2023, 12, 22, 15, 0,0))
                ));

            }
        }
    }
}
