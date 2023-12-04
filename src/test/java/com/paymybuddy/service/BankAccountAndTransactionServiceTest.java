package com.paymybuddy.service;

import com.paymybuddy.domain.dto.BankAccountCreationCommandDTO;
import com.paymybuddy.domain.dto.BankTransactionCommandDTO;
import com.paymybuddy.domain.model.BalanceByCurrencyModel;
import com.paymybuddy.domain.model.BankAccountModel;
import com.paymybuddy.domain.model.BankTransactionModel;
import com.paymybuddy.domain.model.UserAccountModel;
import com.paymybuddy.utils.Fixture;
import com.paymybuddy.utils.UserAccountBuilder;
import org.junit.jupiter.api.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Tag("UnitTest")
public class BankAccountAndTransactionServiceTest {

    @Nested
    @DisplayName("Feature: create a bank account")
    class CreateBankAccount {

        @Nested
        @DisplayName("Rule: no fields should be blank")
        class NoBlankAllowed {
            private Fixture fixture;

            @BeforeEach
            void setUp() {
                fixture = new Fixture();
            }

            @Test
            void itShouldCreateABankAccount() {
                // Given
                UserAccountModel userInDB = new UserAccountBuilder()
                        .withId(UUID.fromString("1124d9e8-6266-4bcf-8035-37a02ba75c69"))
                        .withFirstName("Léo")
                        .withLastName("Minot")
                        .build();

                fixture.givenUserInDatabase(userInDB);

                // When
                fixture.whenRequestForCreateBankAccount(new BankAccountCreationCommandDTO(userInDB, "123456789", "FR"));

                // Then
                fixture.thenItShouldSaveTheBankAccount(new BankAccountModel(null, userInDB, "123456789", "FR"));
            }

            @Test
            void itShouldThrowIfUserNotInDB() {
                // Given
                UserAccountModel userNotInDB = new UserAccountBuilder()
                        .withId(UUID.fromString("1124d9e8-6266-4bcf-8035-37a02ba75c69"))
                        .withFirstName("Léo")
                        .withLastName("Minot")
                        .build();

                // When
                // Then
                fixture.whenRequestForCreateBankAccountAndThenThrow(new BankAccountCreationCommandDTO(userNotInDB, "123456789", "FR"), new RuntimeException("User is unknown"));
            }

            @Test
            void itShouldThrowIfUserAccountIsEmpty() {
                // Given
                // When
                // Then
                fixture.whenRequestForCreateBankAccountAndThenThrow(new BankAccountCreationCommandDTO(null, "123456789", "FR"), new RuntimeException("User must be not null"));
            }

            @Test
            void itShouldThrowIfIbanIsEmpty() {
                // Given
                UserAccountModel userInDB = new UserAccountBuilder()
                        .withId(UUID.fromString("1124d9e8-6266-4bcf-8035-37a02ba75c69"))
                        .withFirstName("Léo")
                        .withLastName("Minot")
                        .build();

                fixture.givenUserInDatabase(userInDB);
                // When
                // Then
                fixture.whenRequestForCreateBankAccountAndThenThrow(new BankAccountCreationCommandDTO(userInDB, null, "FR"), new RuntimeException("IBAN must be not null"));
            }

            @Test
            void itShouldThrowIfCountryIsEmpty() {
                // Given
                UserAccountModel userInDB = new UserAccountBuilder()
                        .withId(UUID.fromString("1124d9e8-6266-4bcf-8035-37a02ba75c69"))
                        .withFirstName("Léo")
                        .withLastName("Minot")
                        .build();

                fixture.givenUserInDatabase(userInDB);
                // When
                // Then
                fixture.whenRequestForCreateBankAccountAndThenThrow(new BankAccountCreationCommandDTO(userInDB, "123456789", null), new RuntimeException("Country must be not null"));
            }
        }

        @Nested
        @DisplayName("Rule: IBAN should be unique")
        class IBANShouldBeUnique {
            private Fixture fixture;

            @BeforeEach
            void setUp() {
                fixture = new Fixture();
            }

            @Test
            void itShouldThrowIfIBANExists() {
                // Given
                UserAccountModel userInDB = new UserAccountBuilder()
                        .withId(UUID.fromString("1124d9e8-6266-4bcf-8035-37a02ba75c69"))
                        .withFirstName("Léo")
                        .withLastName("Minot")
                        .build();

                fixture.givenUserInDatabase(userInDB);

                BankAccountModel bankAccountModel = new BankAccountModel(
                        null,
                        userInDB,
                        "123456789",
                        "FR"
                );
                fixture.givenBankAccountInDatabase(bankAccountModel);
                // When
                // Then
                fixture.whenRequestForCreateBankAccountAndThenThrow(new BankAccountCreationCommandDTO(userInDB, "123456789", "US"), new RuntimeException("IBAN already exists"));
            }
        }

    }

    @Nested
    @DisplayName("Feature: deposit money into my bank account")
    class CreateBankTransaction {

        @Nested
        @DisplayName("Rule: create a balanceByCurrency if not exists and no fields should be blank")
        class CreateABalanceByCurrency {
            private Fixture fixture;

            @BeforeEach
            void setUp() {
                fixture = new Fixture();
            }

            @Test
            void itShouldCreateABalanceByCurrencyIfATransactionISDoneWithUnknownCurrencyForUser() {
                // Given
                UserAccountModel userInDB = new UserAccountBuilder()
                        .withId(UUID.fromString("1124d9e8-6266-4bcf-8035-37a02ba75c69"))
                        .withFirstName("Léo")
                        .withLastName("Minot")
                        .build();

                fixture.givenUserInDatabase(userInDB);

                LocalDateTime now = LocalDateTime.of(2013, 12, 1, 15, 42, 0, 0);
                fixture.givenNowIs(now);

                BankAccountModel bankAccount = new BankAccountModel(
                        UUID.fromString("77777777-6266-4bcf-8035-37a02ba75c69"),
                        userInDB,
                        "123456789",
                        "FR"
                );

                fixture.givenBankAccountInDatabase(bankAccount);

                // When
                fixture.whenCreateANewBankTransaction(new BankTransactionCommandDTO(bankAccount, 100, "EUR"));

                // Then
                fixture.thenBalanceByCurrencyShouldBe(new BalanceByCurrencyModel(null, userInDB, 100, "EUR"));
                fixture.thenABankTransactionShouldBeRegister(new BankTransactionModel(null, bankAccount, 100, "EUR", now));
            }

            @Test
            void itShouldThrowIfBankAccountIsNull() {
                // Given
                // When
                // Then
                fixture.whenCreateABankTransactionAndThenThrow(new BankTransactionCommandDTO(null, 100, "EUR"), new RuntimeException("User Account must be not null"));
            }

            @Test
            void itShouldThrowIfCurrencyIsNull() {
                // Given
                UserAccountModel userInDB = new UserAccountBuilder()
                        .withId(UUID.fromString("1124d9e8-6266-4bcf-8035-37a02ba75c69"))
                        .withFirstName("Léo")
                        .withLastName("Minot")
                        .build();

                fixture.givenUserInDatabase(userInDB);

                LocalDateTime now = LocalDateTime.of(2013, 12, 1, 15, 42, 0, 0);
                fixture.givenNowIs(now);

                BankAccountModel bankAccount = new BankAccountModel(
                        UUID.fromString("77777777-6266-4bcf-8035-37a02ba75c69"),
                        userInDB,
                        "123456789",
                        "FR"
                );

                fixture.givenBankAccountInDatabase(bankAccount);
                // When
                // Then
                fixture.whenCreateABankTransactionAndThenThrow(new BankTransactionCommandDTO(bankAccount, 100, null), new RuntimeException("Currency must be not null"));
            }
        }

        @Nested
        @DisplayName("Rule: increase the amount of a balanceByCurrency if exists")
        class IncreaseBalanceIfTransactionInMoneyThatHasAlreadyABalanceByCurrency {
            private Fixture fixture;

            @BeforeEach
            void setUp() {
                fixture = new Fixture();
            }

            @Test
            void itShouldIncreaseTheBalance() {
                // Given
                UserAccountModel userInDB = new UserAccountBuilder()
                        .withId(UUID.fromString("1124d9e8-6266-4bcf-8035-37a02ba75c69"))
                        .withFirstName("Léo")
                        .withLastName("Minot")
                        .build();

                fixture.givenUserInDatabase(userInDB);

                LocalDateTime now = LocalDateTime.of(2013, 12, 1, 15, 42, 0, 0);
                fixture.givenNowIs(now);

                BankAccountModel bankAccount = new BankAccountModel(
                        UUID.fromString("77777777-6266-4bcf-8035-37a02ba75c69"),
                        userInDB,
                        "123456789",
                        "FR"
                );

                fixture.givenBankAccountInDatabase(bankAccount);

                BankTransactionModel existingBankTransaction = new BankTransactionModel(
                        UUID.fromString("88888888-6266-4bcf-8035-37a02ba75c69"),
                        bankAccount,
                        50,
                        "EUR",
                        LocalDateTime.of(2013, 11, 11, 15, 42, 0, 0)
                );

                fixture.givenTheTransactionInDatabase(existingBankTransaction);

                BalanceByCurrencyModel existingBalanceByCurrency = new BalanceByCurrencyModel(
                        UUID.fromString("99999999-6266-4bcf-8035-37a02ba75c69"),
                        existingBankTransaction.bankAccount().userAccount(),
                        existingBankTransaction.amount(),
                        existingBankTransaction.currency()
                );

                fixture.givenTheBalanceByCurrencyInDataBase(existingBalanceByCurrency);

                // When
                fixture.whenCreateANewBankTransaction(new BankTransactionCommandDTO(bankAccount, 100.0, "EUR"));

                // Then
                fixture.thenBalanceByCurrencyShouldBe(new BalanceByCurrencyModel(UUID.fromString("99999999-6266-4bcf-8035-37a02ba75c69"), userInDB, 150, "EUR"));
                fixture.thenABankTransactionShouldBeRegister(new BankTransactionModel(null, bankAccount, 100, "EUR", now));
                fixture.thenBankTransactionShouldHaveLengthOf(2);
                fixture.thenBalanceByCurrencyShouldHaveLengthOf(1);
            }
        }

        @Nested
        @DisplayName("Rule: It should decrease the balance unless it becomes negative")
        class DecreaseAmountUnlessItBecomesNegative {
            private Fixture fixture;

            @BeforeEach
            void setUp() {
                fixture = new Fixture();
            }
            @Test
            void itShouldDecreaseTheBalance() {
                // Given
                UserAccountModel userInDB = new UserAccountBuilder()
                        .withId(UUID.fromString("1124d9e8-6266-4bcf-8035-37a02ba75c69"))
                        .withFirstName("Léo")
                        .withLastName("Minot")
                        .build();

                fixture.givenUserInDatabase(userInDB);

                LocalDateTime now = LocalDateTime.of(2013, 12, 1, 15, 42, 0, 0);
                fixture.givenNowIs(now);

                BankAccountModel bankAccount = new BankAccountModel(
                        UUID.fromString("77777777-6266-4bcf-8035-37a02ba75c69"),
                        userInDB,
                        "123456789",
                        "FR"
                );

                fixture.givenBankAccountInDatabase(bankAccount);

                BankTransactionModel existingBankTransaction = new BankTransactionModel(
                        UUID.fromString("88888888-6266-4bcf-8035-37a02ba75c69"),
                        bankAccount,
                        50,
                        "EUR",
                        LocalDateTime.of(2013, 11, 11, 15, 42, 0, 0)
                );

                fixture.givenTheTransactionInDatabase(existingBankTransaction);

                BalanceByCurrencyModel existingBalanceByCurrency = new BalanceByCurrencyModel(
                        UUID.fromString("99999999-6266-4bcf-8035-37a02ba75c69"),
                        existingBankTransaction.bankAccount().userAccount(),
                        existingBankTransaction.amount(),
                        existingBankTransaction.currency()
                );

                fixture.givenTheBalanceByCurrencyInDataBase(existingBalanceByCurrency);

                // When
                fixture.whenCreateANewBankTransaction(new BankTransactionCommandDTO(bankAccount, -20.0, "EUR"));

                // Then
                fixture.thenBalanceByCurrencyShouldBe(new BalanceByCurrencyModel(UUID.fromString("99999999-6266-4bcf-8035-37a02ba75c69"), userInDB, 30, "EUR"));
                fixture.thenABankTransactionShouldBeRegister(new BankTransactionModel(null, bankAccount, -20.0, "EUR", now));
                fixture.thenBankTransactionShouldHaveLengthOf(2);
                fixture.thenBalanceByCurrencyShouldHaveLengthOf(1);
            }

            @Test
            void itShouldThrowIfBalanceBecameNegativeWithExistingBalanceByCurrency() {
                // Given
                UserAccountModel userInDB = new UserAccountBuilder()
                        .withId(UUID.fromString("1124d9e8-6266-4bcf-8035-37a02ba75c69"))
                        .withFirstName("Léo")
                        .withLastName("Minot")
                        .build();

                fixture.givenUserInDatabase(userInDB);

                LocalDateTime now = LocalDateTime.of(2013, 12, 1, 15, 42, 0, 0);
                fixture.givenNowIs(now);

                BankAccountModel bankAccount = new BankAccountModel(
                        UUID.fromString("77777777-6266-4bcf-8035-37a02ba75c69"),
                        userInDB,
                        "123456789",
                        "FR"
                );

                fixture.givenBankAccountInDatabase(bankAccount);

                BankTransactionModel existingBankTransaction = new BankTransactionModel(
                        UUID.fromString("88888888-6266-4bcf-8035-37a02ba75c69"),
                        bankAccount,
                        50,
                        "EUR",
                        LocalDateTime.of(2013, 11, 11, 15, 42, 0, 0)
                );

                fixture.givenTheTransactionInDatabase(existingBankTransaction);

                BalanceByCurrencyModel existingBalanceByCurrency = new BalanceByCurrencyModel(
                        UUID.fromString("99999999-6266-4bcf-8035-37a02ba75c69"),
                        existingBankTransaction.bankAccount().userAccount(),
                        existingBankTransaction.amount(),
                        existingBankTransaction.currency()
                );

                fixture.givenTheBalanceByCurrencyInDataBase(existingBalanceByCurrency);

                // When
                // Then
                fixture.whenCreateANewBankTransactionThenThrow(new BankTransactionCommandDTO(bankAccount, -100.0, "EUR"), new RuntimeException("Amount can not go beyond 0"));

            }

            @Test
            void itShouldThrowIfBalanceBecameNegativeWithNoExistingBalanceByCurrency() {
                // Given
                UserAccountModel userInDB = new UserAccountBuilder()
                        .withId(UUID.fromString("1124d9e8-6266-4bcf-8035-37a02ba75c69"))
                        .withFirstName("Léo")
                        .withLastName("Minot")
                        .build();

                fixture.givenUserInDatabase(userInDB);

                LocalDateTime now = LocalDateTime.of(2013, 12, 1, 15, 42, 0, 0);
                fixture.givenNowIs(now);

                BankAccountModel bankAccount = new BankAccountModel(
                        UUID.fromString("77777777-6266-4bcf-8035-37a02ba75c69"),
                        userInDB,
                        "123456789",
                        "FR"
                );

                fixture.givenBankAccountInDatabase(bankAccount);

                // When
                // Then
                fixture.whenCreateANewBankTransactionThenThrow(new BankTransactionCommandDTO(bankAccount, -100.0, "EUR"), new RuntimeException("Amount can not go beyond 0"));
            }
        }
    }




}