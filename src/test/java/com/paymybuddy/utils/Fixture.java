package com.paymybuddy.utils;

import com.paymybuddy.dto.BankAccountCreationCommandDTO;
import com.paymybuddy.dto.BankTransactionCommandDTO;
import com.paymybuddy.dto.UserRequestCommandDTO;
import com.paymybuddy.dto.UserTransactionCommand;
import com.paymybuddy.model.*;
import com.paymybuddy.repository.definition.*;
import com.paymybuddy.service.*;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class Fixture {


    static class StubDateProvider implements DateProvider {

        LocalDateTime now;

        @Override
        public LocalDateTime getNow() {
            return now;
        }

    }
    private final UserAccountRepository userAccountRepository = new FakeUserAccountRepository();
    private final UserRelationRepository userRelationRepository = new FakeUserRelationRepository();
    private final BankAccountRepository bankAccountRepository = new FakeBankAccountRepository();
    private final BalanceByCurrencyRepository balanceByCurrencyRepository = new FakeBalanceByCurrencyRepository();
    private final BankTransactionRepository bankTransactionRepository = new FakeBankTransactionRepository();
    private final UserTransactionRepository userTransactionRepository = new FakeUserTransactionRepository();
    private final UserTransferRepository userTransferRepository = new FakeUserTransferRepository();
    private final StubDateProvider dateProvider = new StubDateProvider();
    private UserAccount userAccountToCreate;

    private UserRequestCommandDTO userRequestCommandDTO;
    private final BalanceByCurrencyService balanceByCurrencyService = new BalanceByCurrencyService(balanceByCurrencyRepository);
    private final UserAccountService userAccountService = new UserAccountService(userAccountRepository);
    private final UserRelationService userRelationService = new UserRelationService(userRelationRepository, dateProvider);

    private final BankAccountService bankAccountService = new BankAccountService(bankAccountRepository, userAccountRepository);
    private final BankTransactionService bankTransactionService = new BankTransactionService(balanceByCurrencyService, bankTransactionRepository, dateProvider);
    private final UserTransactionService userTransactionService = new UserTransactionService(balanceByCurrencyService, userTransactionRepository, userTransferRepository, dateProvider);
    public void givenUserInDatabase(UserAccount userInDB) {
        userAccountRepository.save(userInDB);
    }
    public void givenNowIs(LocalDateTime now) {
        dateProvider.now = now;
    }
    public void givenBankAccountInDatabase(BankAccount bankAccount) {
        bankAccountRepository.save(bankAccount);
    }

    public void givenTheTransactionInDatabase(BankTransaction existingBankTransaction) {
        bankTransactionRepository.save(existingBankTransaction);

    }
    public void givenTheBalanceByCurrencyInDataBase(BalanceByCurrency existingBalanceByCurrency) {
        balanceByCurrencyRepository.save(existingBalanceByCurrency);
    }
    public void whenRequestForCreateBankAccount(BankAccountCreationCommandDTO bankAccountCreationCommandDTO) {
        bankAccountService.create(bankAccountCreationCommandDTO);
    }
    public void whenRequestForCreateUser(UserRequestCommandDTO userRequestCommandDTO) {
        userAccountToCreate = userAccountService.createUserAccount(userRequestCommandDTO);
    }

    public void whenRequestForCreateUserThatThrow(UserRequestCommandDTO userRequestCommandDTO) {
        this.userRequestCommandDTO = userRequestCommandDTO;
    }

    public void whenRequestACreationOfARelationBetween(UserAccount user1, UserAccount user2) {
        userRelationService.createRelation(user1, user2);
    }

    public void whenRequestACreationOfARelationBetweenThenThrow(UserAccount user1, UserAccount user2, String message) {
        assertThatThrownBy(() -> userRelationService.createRelation(user1, user2))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining(message);
    }

    public void whenRequestForCreateBankAccountAndThenThrow(BankAccountCreationCommandDTO bankAccountCreationCommandDTO, Exception exceptionToThrown) {
        assertThatThrownBy(() -> bankAccountService.create(bankAccountCreationCommandDTO))
                .isInstanceOf(exceptionToThrown.getClass())
                .hasMessageContaining(exceptionToThrown.getMessage());
    }

    public void whenCreateANewBankTransaction(BankTransactionCommandDTO bankTransactionCommand) {
        bankTransactionService.newTransaction(bankTransactionCommand);
    }

    public void whenCreateABankTransactionAndThenThrow(BankTransactionCommandDTO bankTransactionCommand, RuntimeException exceptionToThrown) {
        assertThatThrownBy(() -> bankTransactionService.newTransaction(bankTransactionCommand))
                .isInstanceOf(exceptionToThrown.getClass())
                .hasMessageContaining(exceptionToThrown.getMessage());
    }

    public void whenCreateANewBankTransactionThenThrow(BankTransactionCommandDTO bankTransactionCommand, RuntimeException exceptionToThrow) {
        assertThatThrownBy(() -> bankTransactionService.newTransaction(bankTransactionCommand))
                .isInstanceOf(exceptionToThrow.getClass())
                .hasMessageContaining(exceptionToThrow.getMessage());
    }

    public void whenCreateATransactionBetweenUsers(UserAccount fromUser, UserAccount toUser, String description, String currency, double amount) {
        userTransactionService.performTransaction(new UserTransactionCommand(fromUser, toUser, description, currency,amount));
    }

    public void whenCreateATransactionBetweenUsersAndThenThrow(UserAccount fromUser, UserAccount toUser, String description, String currency, double amount, RuntimeException exceptionThrown) {
        assertThatThrownBy(() -> userTransactionService.performTransaction(new UserTransactionCommand(fromUser, toUser, description, currency, amount)))
                .isInstanceOf(exceptionThrown.getClass())
                .hasMessageContaining(exceptionThrown.getMessage());
    }

    public void thenTheUserShouldBeAndSaved(UserAccount expectedUserAccount) {
        assertThat(userAccountRepository.get(userAccountToCreate).get()).isEqualTo(expectedUserAccount);
    }

    public void thenItShouldThrowAnException(RuntimeException e) {
        assertThatThrownBy(() -> userAccountService.createUserAccount(this.userRequestCommandDTO))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining(e.getMessage());
    }

    public void thenARelationHasToBeCreateAndEqualTo(Relation expectedRelation) {
        assertThat(userRelationRepository.getRelation(expectedRelation.getUser1(), expectedRelation.getUser2())).isEqualTo(expectedRelation);
    }

    public void thenItShouldSaveTheBankAccount(BankAccount bankAccount) {
        assertThat(bankAccountRepository.get(bankAccount)).isEqualTo(bankAccount);
    }

    public void thenBalanceByCurrencyShouldBe(BalanceByCurrency balanceByCurrency) {
        assertThat(balanceByCurrencyRepository.get(balanceByCurrency)).isEqualTo(balanceByCurrency);
        System.out.println(balanceByCurrencyRepository.get(balanceByCurrency));
    }

    public void thenBalanceByCurrencyShouldBeWithAmountVerification(BalanceByCurrency balanceByCurrency) {
        assertThat(balanceByCurrencyRepository.get(balanceByCurrency)).isEqualTo(balanceByCurrency);
        assertThat(balanceByCurrencyRepository.get(balanceByCurrency).getBalance()).isEqualTo(balanceByCurrency.getBalance());
    }

    public void thenABankTransactionShouldBeRegister(BankTransaction bankTransaction) {
        assertThat(bankTransactionRepository.get(bankTransaction)).isEqualTo(bankTransaction);
    }

    public void thenBankTransactionShouldHaveLengthOf(int size) {
        assertThat(bankTransactionRepository.getAll()).hasSize(size);
    }

    public void thenBalanceByCurrencyShouldHaveLengthOf(int size) {
        assertThat(balanceByCurrencyRepository.getAll()).hasSize(size);
    }

    public void thenItShouldCreateATransactionOf(Transaction transactionModel) {
        assertThat(userTransactionRepository.get(transactionModel)).isEqualTo(transactionModel);
    }

    public void thenItShouldCreateATransferOf(Transfer transferModel) {
        assertThat(userTransferRepository.get(transferModel)).isEqualTo(transferModel);
    }
}

