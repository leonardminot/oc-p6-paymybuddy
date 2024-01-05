package com.paymybuddy.service;

import com.paymybuddy.Exception.BalanceAndTransferException;
import com.paymybuddy.Exception.EmptyFieldException;
import com.paymybuddy.Exception.IBANException;
import com.paymybuddy.Exception.UserException;
import com.paymybuddy.model.*;
import com.paymybuddy.dto.BankAccountCreationCommandDTO;
import com.paymybuddy.dto.BankTransactionCommandDTO;
import com.paymybuddy.utils.Fixture;
import com.paymybuddy.utils.UserAccountBuilder;
import org.junit.jupiter.api.*;

import java.time.LocalDateTime;
import java.util.List;
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
                UserAccount userInDB = new UserAccountBuilder()
                        .withId(UUID.fromString("1124d9e8-6266-4bcf-8035-37a02ba75c69"))
                        .withFirstName("Léo")
                        .withLastName("Minot")
                        .build();

                fixture.givenUserInDatabase(userInDB);

                // When
                fixture.whenRequestForCreateBankAccount(new BankAccountCreationCommandDTO(userInDB, "123456789", "FR"));

                // Then
                fixture.thenItShouldSaveTheBankAccount(new BankAccount(UUID.fromString("00000000-0000-0000-0000-000000000000"), userInDB, "123456789", "FR"));
            }

            @Test
            void itShouldThrowIfUserNotInDB() {
                // Given
                UserAccount userNotInDB = new UserAccountBuilder()
                        .withId(UUID.fromString("1124d9e8-6266-4bcf-8035-37a02ba75c69"))
                        .withFirstName("Léo")
                        .withLastName("Minot")
                        .build();

                // When
                // Then
                fixture.whenRequestForCreateBankAccountAndThenThrow(new BankAccountCreationCommandDTO(userNotInDB, "123456789", "FR"), new UserException("User is unknown"));
            }

            @Test
            void itShouldThrowIfUserAccountIsEmpty() {
                // Given
                // When
                // Then
                fixture.whenRequestForCreateBankAccountAndThenThrow(new BankAccountCreationCommandDTO(null, "123456789", "FR"), new UserException("User must be not null"));
            }

            @Test
            void itShouldThrowIfIbanIsEmpty() {
                // Given
                UserAccount userInDB = new UserAccountBuilder()
                        .withId(UUID.fromString("1124d9e8-6266-4bcf-8035-37a02ba75c69"))
                        .withFirstName("Léo")
                        .withLastName("Minot")
                        .build();

                fixture.givenUserInDatabase(userInDB);
                // When
                // Then
                fixture.whenRequestForCreateBankAccountAndThenThrow(new BankAccountCreationCommandDTO(userInDB, null, "FR"), new EmptyFieldException("IBAN must be not null"));
            }

            @Test
            void itShouldThrowIfCountryIsEmpty() {
                // Given
                UserAccount userInDB = new UserAccountBuilder()
                        .withId(UUID.fromString("1124d9e8-6266-4bcf-8035-37a02ba75c69"))
                        .withFirstName("Léo")
                        .withLastName("Minot")
                        .build();

                fixture.givenUserInDatabase(userInDB);
                // When
                // Then
                fixture.whenRequestForCreateBankAccountAndThenThrow(new BankAccountCreationCommandDTO(userInDB, "123456789", null), new EmptyFieldException("Country must be not null"));
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
                UserAccount userInDB = new UserAccountBuilder()
                        .withId(UUID.fromString("1124d9e8-6266-4bcf-8035-37a02ba75c69"))
                        .withFirstName("Léo")
                        .withLastName("Minot")
                        .build();

                fixture.givenUserInDatabase(userInDB);

                BankAccount bankAccountModel = new BankAccount(
                        UUID.fromString("00000000-0000-0000-0000-000000000000"),
                        userInDB,
                        "123456789",
                        "FR"
                );
                fixture.givenBankAccountInDatabase(bankAccountModel);
                // When
                // Then
                fixture.whenRequestForCreateBankAccountAndThenThrow(new BankAccountCreationCommandDTO(userInDB, "123456789", "US"), new IBANException("IBAN already exists"));
            }
        }

    }

    @Nested
    @DisplayName("Feature: it should return the list or the given Bank Accounts")
    class GetBankAccounts {

        @Nested
        @DisplayName("Rule: it should return all banks account associate to an user")
        class GetAllBankAccountsAssociatedToAnUser {
            private Fixture fixture;

            @BeforeEach
            void setUp() {
                fixture = new Fixture();
            }

            @Test
            void itShouldReturnAllBankAccounts() {
                // Given
                UserAccount user1 = new UserAccountBuilder()
                        .withId(UUID.fromString("1124d9e8-6266-4bcf-8035-37a02ba75c69"))
                        .withFirstName("Léo")
                        .withLastName("Minot")
                        .build();

                UserAccount user2 = new UserAccountBuilder()
                        .withId(UUID.fromString("2124d9e8-6266-4bcf-8035-37a02ba75c69"))
                        .withFirstName("Victor")
                        .withLastName("Minot")
                        .build();

                fixture.givenUserInDatabase(user1);
                fixture.givenUserInDatabase(user2);

                BankAccount bankAccount1 = new BankAccount(
                        UUID.fromString("77777777-6266-4bcf-8035-37a02ba75c69"),
                        user1,
                        "123456789",
                        "FR"
                );

                BankAccount bankAccount2 = new BankAccount(
                        UUID.fromString("77777777-7777-4bcf-8035-37a02ba75c69"),
                        user2,
                        "333333333",
                        "FR"
                );

                BankAccount bankAccount3 = new BankAccount(
                        UUID.fromString("77777777-8888-4bcf-8035-37a02ba75c69"),
                        user1,
                        "444444444",
                        "FR"
                );



                fixture.givenBankAccountInDatabase(bankAccount1);
                fixture.givenBankAccountInDatabase(bankAccount2);
                fixture.givenBankAccountInDatabase(bankAccount3);

                // When
                fixture.whenFetchAllBankAccountsForUser(user1);

                // Then
                fixture.thenItShouldTheReturnListOfBankAccounts(List.of(bankAccount1, bankAccount3));
            }
        }

        @Nested
        @DisplayName("Rule: it should return the bank account with the given id")
        class FetchGivenBankAccount {
            private Fixture fixture;

            @BeforeEach
            void setUp() {
                fixture = new Fixture();
            }

            @Test
            void itShouldReturnTheBankAccount() {
                // Given
                UserAccount user = new UserAccountBuilder()
                        .withId(UUID.fromString("1124d9e8-6266-4bcf-8035-37a02ba75c69"))
                        .withFirstName("Léo")
                        .withLastName("Minot")
                        .build();

                BankAccount bankAccount = new BankAccount(
                        UUID.fromString("77777777-6266-4bcf-8035-37a02ba75c69"),
                        user,
                        "123456789",
                        "FR"
                );

                fixture.givenUserInDatabase(user);
                fixture.givenBankAccountInDatabase(bankAccount);

                // When
                fixture.whenFetchBankAccountWithId(bankAccount.getBankAccountId());

                // Then
                fixture.thenItShouldReturnTheBankAccount(bankAccount);

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
                UserAccount userInDB = new UserAccountBuilder()
                        .withId(UUID.fromString("1124d9e8-6266-4bcf-8035-37a02ba75c69"))
                        .withFirstName("Léo")
                        .withLastName("Minot")
                        .build();

                fixture.givenUserInDatabase(userInDB);

                LocalDateTime now = LocalDateTime.of(2013, 12, 1, 15, 42, 0, 0);
                fixture.givenNowIs(now);

                BankAccount bankAccount = new BankAccount(
                        UUID.fromString("77777777-6266-4bcf-8035-37a02ba75c69"),
                        userInDB,
                        "123456789",
                        "FR"
                );

                fixture.givenBankAccountInDatabase(bankAccount);

                // When
                fixture.whenCreateANewBankTransaction(new BankTransactionCommandDTO(bankAccount, 100, Currency.EUR));

                // Then
                fixture.thenBalanceByCurrencyShouldBe(new BalanceByCurrency(UUID.fromString("00000000-0000-0000-0000-000000000000"), userInDB, 100.0, Currency.EUR));
                fixture.thenABankTransactionShouldBeRegister(new BankTransaction(UUID.fromString("00000000-0000-0000-0000-000000000000"), bankAccount, 100.0, Currency.EUR, now));
            }

            @Test
            void itShouldThrowIfBankAccountIsNull() {
                // Given
                // When
                // Then
                fixture.whenCreateABankTransactionAndThenThrow(new BankTransactionCommandDTO(null, 100, Currency.EUR), new UserException("User Account must be not null"));
            }

            @Test
            void itShouldThrowIfCurrencyIsNull() {
                // Given
                UserAccount userInDB = new UserAccountBuilder()
                        .withId(UUID.fromString("1124d9e8-6266-4bcf-8035-37a02ba75c69"))
                        .withFirstName("Léo")
                        .withLastName("Minot")
                        .build();

                fixture.givenUserInDatabase(userInDB);

                LocalDateTime now = LocalDateTime.of(2013, 12, 1, 15, 42, 0, 0);
                fixture.givenNowIs(now);

                BankAccount bankAccount = new BankAccount(
                        UUID.fromString("77777777-6266-4bcf-8035-37a02ba75c69"),
                        userInDB,
                        "123456789",
                        "FR"
                );

                fixture.givenBankAccountInDatabase(bankAccount);
                // When
                // Then
                fixture.whenCreateABankTransactionAndThenThrow(new BankTransactionCommandDTO(bankAccount, 100, null), new EmptyFieldException("Currency must be not null"));
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
                UserAccount userInDB = new UserAccountBuilder()
                        .withId(UUID.fromString("1124d9e8-6266-4bcf-8035-37a02ba75c69"))
                        .withFirstName("Léo")
                        .withLastName("Minot")
                        .build();

                fixture.givenUserInDatabase(userInDB);

                LocalDateTime now = LocalDateTime.of(2013, 12, 1, 15, 42, 0, 0);
                fixture.givenNowIs(now);

                BankAccount bankAccount = new BankAccount(
                        UUID.fromString("77777777-6266-4bcf-8035-37a02ba75c69"),
                        userInDB,
                        "123456789",
                        "FR"
                );

                fixture.givenBankAccountInDatabase(bankAccount);

                BankTransaction existingBankTransaction = new BankTransaction(
                        UUID.fromString("88888888-6266-4bcf-8035-37a02ba75c69"),
                        bankAccount,
                        50.0,
                        Currency.EUR,
                        LocalDateTime.of(2013, 11, 11, 15, 42, 0, 0)
                );

                fixture.givenTheTransactionInDatabase(existingBankTransaction);

                BalanceByCurrency existingBalanceByCurrency = new BalanceByCurrency(
                        UUID.fromString("99999999-6266-4bcf-8035-37a02ba75c69"),
                        existingBankTransaction.getBankAccount().getUserAccount(),
                        existingBankTransaction.getAmount(),
                        existingBankTransaction.getCurrency()
                );

                fixture.givenTheBalanceByCurrencyInDataBase(existingBalanceByCurrency);

                // When
                fixture.whenCreateANewBankTransaction(new BankTransactionCommandDTO(bankAccount, 100.0, Currency.EUR));

                // Then
                fixture.thenBalanceByCurrencyShouldBeWithAmountVerification(new BalanceByCurrency(UUID.fromString("99999999-6266-4bcf-8035-37a02ba75c69"), userInDB, 150.0, Currency.EUR));
                fixture.thenABankTransactionShouldBeRegister(new BankTransaction(UUID.fromString("00000000-0000-0000-0000-000000000000"), bankAccount, 100.0, Currency.EUR, now));
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
                UserAccount userInDB = new UserAccountBuilder()
                        .withId(UUID.fromString("1124d9e8-6266-4bcf-8035-37a02ba75c69"))
                        .withFirstName("Léo")
                        .withLastName("Minot")
                        .build();

                fixture.givenUserInDatabase(userInDB);

                LocalDateTime now = LocalDateTime.of(2013, 12, 1, 15, 42, 0, 0);
                fixture.givenNowIs(now);

                BankAccount bankAccount = new BankAccount(
                        UUID.fromString("77777777-6266-4bcf-8035-37a02ba75c69"),
                        userInDB,
                        "123456789",
                        "FR"
                );

                fixture.givenBankAccountInDatabase(bankAccount);

                BankTransaction existingBankTransaction = new BankTransaction(
                        UUID.fromString("88888888-6266-4bcf-8035-37a02ba75c69"),
                        bankAccount,
                        50.0,
                        Currency.EUR,
                        LocalDateTime.of(2013, 11, 11, 15, 42, 0, 0)
                );

                fixture.givenTheTransactionInDatabase(existingBankTransaction);

                BalanceByCurrency existingBalanceByCurrency = new BalanceByCurrency(
                        UUID.fromString("99999999-6266-4bcf-8035-37a02ba75c69"),
                        existingBankTransaction.getBankAccount().getUserAccount(),
                        existingBankTransaction.getAmount(),
                        existingBankTransaction.getCurrency()
                );

                fixture.givenTheBalanceByCurrencyInDataBase(existingBalanceByCurrency);

                // When
                fixture.whenCreateANewBankTransaction(new BankTransactionCommandDTO(bankAccount, -20.0, Currency.EUR));

                // Then
                fixture.thenBalanceByCurrencyShouldBeWithAmountVerification(new BalanceByCurrency(UUID.fromString("99999999-6266-4bcf-8035-37a02ba75c69"), userInDB, 30.0, Currency.EUR));
                fixture.thenABankTransactionShouldBeRegister(new BankTransaction(UUID.fromString("00000000-0000-0000-0000-000000000000"), bankAccount, -20.0, Currency.EUR, now));
                fixture.thenBankTransactionShouldHaveLengthOf(2);
                fixture.thenBalanceByCurrencyShouldHaveLengthOf(1);
            }

            @Test
            void itShouldThrowIfBalanceBecameNegativeWithExistingBalanceByCurrency() {
                // Given
                UserAccount userInDB = new UserAccountBuilder()
                        .withId(UUID.fromString("1124d9e8-6266-4bcf-8035-37a02ba75c69"))
                        .withFirstName("Léo")
                        .withLastName("Minot")
                        .build();

                fixture.givenUserInDatabase(userInDB);

                LocalDateTime now = LocalDateTime.of(2013, 12, 1, 15, 42, 0, 0);
                fixture.givenNowIs(now);

                BankAccount bankAccount = new BankAccount(
                        UUID.fromString("77777777-6266-4bcf-8035-37a02ba75c69"),
                        userInDB,
                        "123456789",
                        "FR"
                );

                fixture.givenBankAccountInDatabase(bankAccount);

                BankTransaction existingBankTransaction = new BankTransaction(
                        UUID.fromString("88888888-6266-4bcf-8035-37a02ba75c69"),
                        bankAccount,
                        50.0,
                        Currency.EUR,
                        LocalDateTime.of(2013, 11, 11, 15, 42, 0, 0)
                );

                fixture.givenTheTransactionInDatabase(existingBankTransaction);

                BalanceByCurrency existingBalanceByCurrency = new BalanceByCurrency(
                        UUID.fromString("99999999-6266-4bcf-8035-37a02ba75c69"),
                        existingBankTransaction.getBankAccount().getUserAccount(),
                        existingBankTransaction.getAmount(),
                        existingBankTransaction.getCurrency()
                );

                fixture.givenTheBalanceByCurrencyInDataBase(existingBalanceByCurrency);

                // When
                // Then
                fixture.whenCreateANewBankTransactionThenThrow(new BankTransactionCommandDTO(bankAccount, -100.0, Currency.EUR), new BalanceAndTransferException("Amount can not go beyond 0"));

            }

            @Test
            void itShouldThrowIfBalanceBecameNegativeWithNoExistingBalanceByCurrency() {
                // Given
                UserAccount userInDB = new UserAccountBuilder()
                        .withId(UUID.fromString("1124d9e8-6266-4bcf-8035-37a02ba75c69"))
                        .withFirstName("Léo")
                        .withLastName("Minot")
                        .build();

                fixture.givenUserInDatabase(userInDB);

                LocalDateTime now = LocalDateTime.of(2013, 12, 1, 15, 42, 0, 0);
                fixture.givenNowIs(now);

                BankAccount bankAccount = new BankAccount(
                        UUID.fromString("77777777-6266-4bcf-8035-37a02ba75c69"),
                        userInDB,
                        "123456789",
                        "FR"
                );

                fixture.givenBankAccountInDatabase(bankAccount);

                // When
                // Then
                fixture.whenCreateANewBankTransactionThenThrow(new BankTransactionCommandDTO(bankAccount, -100.0, Currency.EUR), new BalanceAndTransferException("Amount can not go beyond 0"));
            }
        }
    }

    @Nested
    @DisplayName("Feature: it should return bank transactions for a User")
    class getBankTransaction {

        @Nested
        @DisplayName("Rule: it should return all transaction for a given user")
        class getBankTransactionForGivenUser {
            private Fixture fixture;

            @BeforeEach
            void setUp() {
                fixture = new Fixture();
            }

            @Test
            void itShouldReturnAListOfBankTransaction() {
                // Given
                UserAccount targetUser = new UserAccountBuilder()
                        .withId(UUID.fromString("1124d9e8-6266-4bcf-8035-37a02ba75c69"))
                        .withFirstName("Léo")
                        .withLastName("Minot")
                        .build();

                fixture.givenUserInDatabase(targetUser);

                UserAccount otherUser = new UserAccountBuilder()
                        .withId(UUID.fromString("1124d9e8-9999-4bcf-8035-37a02ba75c69"))
                        .withFirstName("Victor")
                        .withLastName("Minot")
                        .build();

                fixture.givenUserInDatabase(otherUser);

                BankAccount bankAccount1 = new BankAccount(
                        UUID.fromString("77777777-6266-4bcf-8035-37a02ba75c69"),
                        targetUser,
                        "123456789",
                        "FR"
                );

                BankAccount bankAccount2 = new BankAccount(
                        UUID.fromString("77777777-7777-4bcf-8035-37a02ba75c69"),
                        targetUser,
                        "333333333",
                        "FR"
                );

                BankAccount bankAccount3 = new BankAccount(
                        UUID.fromString("77777777-8888-4bcf-8035-37a02ba75c69"),
                        otherUser,
                        "789456123",
                        "FR"
                );

                fixture.givenBankAccountInDatabase(bankAccount1);
                fixture.givenBankAccountInDatabase(bankAccount2);
                fixture.givenBankAccountInDatabase(bankAccount3);

                BankTransaction bankTransaction1 = new BankTransaction(
                        UUID.fromString("00000000-6266-4bcf-8035-37a02ba75c69"),
                        bankAccount1,
                        100.0,
                        Currency.EUR,
                        LocalDateTime.now()
                );

                BankTransaction bankTransaction2 = new BankTransaction(
                        UUID.fromString("00000001-6266-4bcf-8035-37a02ba75c69"),
                        bankAccount2,
                        150.0,
                        Currency.EUR,
                        LocalDateTime.now()
                );

                BankTransaction bankTransaction3 = new BankTransaction(
                        UUID.fromString("00000002-6266-4bcf-8035-37a02ba75c69"),
                        bankAccount3,
                        100.0,
                        Currency.USD,
                        LocalDateTime.now()
                );

                BankTransaction bankTransaction4 = new BankTransaction(
                        UUID.fromString("00000003-6266-4bcf-8035-37a02ba75c69"),
                        bankAccount1,
                        -32.0,
                        Currency.USD,
                        LocalDateTime.now()
                );

                fixture.givenTheTransactionInDatabase(bankTransaction1);
                fixture.givenTheTransactionInDatabase(bankTransaction2);
                fixture.givenTheTransactionInDatabase(bankTransaction3);
                fixture.givenTheTransactionInDatabase(bankTransaction4);

                // When
                fixture.whenFetchBankTransactionFor(targetUser);

                // Then
                fixture.thenTransactionListShouldContain(List.of(bankTransaction1, bankTransaction2, bankTransaction4));

            }
        }
    }




}
