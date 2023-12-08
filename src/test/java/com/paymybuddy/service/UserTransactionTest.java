package com.paymybuddy.service;

import com.paymybuddy.domain.model.BalanceByCurrencyModel;
import com.paymybuddy.domain.model.TransactionModel;
import com.paymybuddy.domain.model.TransferModel;
import com.paymybuddy.domain.model.UserAccountModel;
import com.paymybuddy.utils.Fixture;
import com.paymybuddy.utils.UserAccountBuilder;
import org.junit.jupiter.api.*;

import java.time.LocalDateTime;
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
                UserAccountModel fromUser = new UserAccountBuilder()
                        .withId(UUID.fromString("1124d9e8-6266-4bcf-8035-37a02ba75c69"))
                        .withFirstName("Léo")
                        .withLastName("Minot")
                        .build();

                fixture.givenUserInDatabase(fromUser);

                LocalDateTime now = LocalDateTime.of(2013, 12, 1, 15, 42, 0, 0);
                fixture.givenNowIs(now);


                UserAccountModel toUser = new UserAccountBuilder()
                        .withId(UUID.fromString("22222222-6266-4bcf-8035-37a02ba75c69"))
                        .withFirstName("Victor")
                        .withLastName("Minot")
                        .build();

                fixture.givenUserInDatabase(toUser);

                // When
                fixture.whenCreateATransactionBetweenUsers(fromUser, toUser, "test transaction", "EUR", 100.0);

                // Then
                fixture.thenItShouldCreateATransactionOf(new TransactionModel(null, "test transaction", 100.0, "EUR", now));
            }

            @Test
            void itShouldThrowIfDescriptionIsNull() {
                // Given
                UserAccountModel fromUser = new UserAccountBuilder()
                        .withId(UUID.fromString("1124d9e8-6266-4bcf-8035-37a02ba75c69"))
                        .withFirstName("Léo")
                        .withLastName("Minot")
                        .build();

                fixture.givenUserInDatabase(fromUser);

                LocalDateTime now = LocalDateTime.of(2013, 12, 1, 15, 42, 0, 0);

                UserAccountModel toUser = new UserAccountBuilder()
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
                UserAccountModel fromUser = new UserAccountBuilder()
                        .withId(UUID.fromString("1124d9e8-6266-4bcf-8035-37a02ba75c69"))
                        .withFirstName("Léo")
                        .withLastName("Minot")
                        .build();

                fixture.givenUserInDatabase(fromUser);

                LocalDateTime now = LocalDateTime.of(2013, 12, 1, 15, 42, 0, 0);

                UserAccountModel toUser = new UserAccountBuilder()
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

                UserAccountModel toUser = new UserAccountBuilder()
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
                UserAccountModel fromUser = new UserAccountBuilder()
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
                UserAccountModel fromUser = new UserAccountBuilder()
                        .withId(UUID.fromString("1124d9e8-6266-4bcf-8035-37a02ba75c69"))
                        .withFirstName("Léo")
                        .withLastName("Minot")
                        .build();

                fixture.givenUserInDatabase(fromUser);

                LocalDateTime now = LocalDateTime.of(2013, 12, 1, 15, 42, 0, 0);
                fixture.givenNowIs(now);

                UserAccountModel toUser = new UserAccountBuilder()
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
                UserAccountModel fromUser = new UserAccountBuilder()
                        .withId(UUID.fromString("1124d9e8-6266-4bcf-8035-37a02ba75c69"))
                        .withFirstName("Léo")
                        .withLastName("Minot")
                        .build();

                fixture.givenUserInDatabase(fromUser);

                LocalDateTime now = LocalDateTime.of(2013, 12, 1, 15, 42, 0, 0);
                fixture.givenNowIs(now);


                UserAccountModel toUser = new UserAccountBuilder()
                        .withId(UUID.fromString("22222222-6266-4bcf-8035-37a02ba75c69"))
                        .withFirstName("Victor")
                        .withLastName("Minot")
                        .build();

                fixture.givenUserInDatabase(toUser);

                // When
                fixture.whenCreateATransactionBetweenUsers(fromUser, toUser, "test transaction", "EUR", 100.0);

                // Then
                fixture.thenItShouldCreateATransferOf(new TransferModel(fromUser, toUser, new TransactionModel(null, "test transaction", 100.0, "EUR", now)));
            }
        }
    }

    @Nested
    @DisplayName("Feature: modify the balance by currency when performing a user transaction")
    class ModifyBalanceByCurrency {
        private Fixture fixture;

        @BeforeEach
        void setUp() {
            fixture = new Fixture();
        }

        @Nested
        @DisplayName("Rule: it should create or update a balance by currency for the toUser if not exists")
        class CreateABalanceByCurrencyForTheToUser {
            @Test
            void itShouldCreateABalanceByCurrencyForToUser() {
                // Given
                UserAccountModel fromUser = new UserAccountBuilder()
                        .withId(UUID.fromString("1124d9e8-6266-4bcf-8035-37a02ba75c69"))
                        .withFirstName("Léo")
                        .withLastName("Minot")
                        .build();

                fixture.givenUserInDatabase(fromUser);



                UserAccountModel toUser = new UserAccountBuilder()
                        .withId(UUID.fromString("22222222-6266-4bcf-8035-37a02ba75c69"))
                        .withFirstName("Victor")
                        .withLastName("Minot")
                        .build();

                fixture.givenUserInDatabase(toUser);

                LocalDateTime now = LocalDateTime.of(2013, 12, 1, 15, 42, 0, 0);
                fixture.givenNowIs(now);

                // When
                fixture.whenCreateATransactionBetweenUsers(fromUser, toUser, "Transaction description", "USD", 100.0);

                // Then
                fixture.thenBalanceByCurrencyShouldBe(new BalanceByCurrencyModel(
                        null,
                        toUser,
                        100.0,
                        "USD"
                ));

            }

            @Test
            void itShouldUpdateABalanceByCurrencyForTheToUser() {
                // Given
                UserAccountModel fromUser = new UserAccountBuilder()
                        .withId(UUID.fromString("1124d9e8-6266-4bcf-8035-37a02ba75c69"))
                        .withFirstName("Léo")
                        .withLastName("Minot")
                        .build();

                fixture.givenUserInDatabase(fromUser);

                UserAccountModel toUser = new UserAccountBuilder()
                        .withId(UUID.fromString("22222222-6266-4bcf-8035-37a02ba75c69"))
                        .withFirstName("Victor")
                        .withLastName("Minot")
                        .build();

                fixture.givenUserInDatabase(toUser);

                LocalDateTime now = LocalDateTime.of(2013, 12, 1, 15, 42, 0, 0);
                fixture.givenNowIs(now);

                BalanceByCurrencyModel existingBalanceByCurrency = new BalanceByCurrencyModel(
                        UUID.fromString("33333333-6266-4bcf-8035-37a02ba75c69"),
                        toUser,
                        50.0,
                        "USD"
                );
                fixture.givenTheBalanceByCurrencyInDataBase(existingBalanceByCurrency);

                // When
                fixture.whenCreateATransactionBetweenUsers(fromUser, toUser, "Transaction description", "USD", 100.0);

                // Then
                fixture.thenBalanceByCurrencyShouldBe(new BalanceByCurrencyModel(
                        existingBalanceByCurrency.id(),
                        toUser,
                        150.0,
                        "USD"
                ));
                fixture.thenBalanceByCurrencyShouldHaveLengthOf(1);
            }
        }
    }
}
