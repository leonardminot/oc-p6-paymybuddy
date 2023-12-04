package com.paymybuddy.utils;

import com.paymybuddy.domain.dto.BankAccountCreationCommandDTO;
import com.paymybuddy.domain.dto.BankTransactionCommandDTO;
import com.paymybuddy.domain.dto.UserRequestCommandDTO;
import com.paymybuddy.domain.model.*;
import com.paymybuddy.domain.repository.*;
import com.paymybuddy.domain.service.*;

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
    private final StubDateProvider dateProvider = new StubDateProvider();

    private UserAccountModel userAccountToCreate;
    private UserRequestCommandDTO userRequestCommandDTO;
    private final UserAccountService userAccountService = new UserAccountService(userAccountRepository);
    private final UserRelationService userRelationService = new UserRelationService(userRelationRepository, dateProvider);
    private final BankAccountService bankAccountService = new BankAccountService(bankAccountRepository, userAccountRepository);

    private final BankTransactionService bankTransactionService = new BankTransactionService(bankTransactionRepository, balanceByCurrencyRepository, dateProvider);
    public void givenUserInDatabase(UserAccountModel userInDB) {
        userAccountRepository.save(userInDB);
    }
    public void givenNowIs(LocalDateTime now) {
        dateProvider.now = now;
    }
    public void givenBankAccountInDatabase(BankAccountModel bankAccount) {
        bankAccountRepository.save(bankAccount);
    }

    public void givenTheTransactionInDatabase(BankTransactionModel existingBankTransaction) {
        bankTransactionRepository.save(existingBankTransaction);

    }

    public void givenTheBalanceByCurrencyInDataBase(BalanceByCurrencyModel existingBalanceByCurrency) {
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

    public void whenRequestACreationOfARelationBetween(UserAccountModel user1, UserAccountModel user2) {
        userRelationService.createRelation(user1, user2);
    }

    public void whenRequestACreationOfARelationBetweenThenThrow(UserAccountModel user1, UserAccountModel user2, String message) {
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

    public void thenTheUserShouldBeAndSaved(UserAccountModel expectedUserAccount) {
        assertThat(userAccountRepository.get(userAccountToCreate).get()).isEqualTo(expectedUserAccount);
    }

    public void thenItShouldThrowAnException(RuntimeException e) {
        assertThatThrownBy(() -> userAccountService.createUserAccount(this.userRequestCommandDTO))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining(e.getMessage());
    }

    public void thenARelationHasToBeCreateAndEqualTo(UserRelationModel expectedRelation) {
        assertThat(userRelationRepository.getRelation(expectedRelation.user1(), expectedRelation.user2())).isEqualTo(expectedRelation);
    }

    public void thenItShouldSaveTheBankAccount(BankAccountModel bankAccount) {
        assertThat(bankAccountRepository.get(bankAccount)).isEqualTo(bankAccount);
    }

    public void thenBalanceByCurrencyShouldBe(BalanceByCurrencyModel balanceByCurrency) {
        assertThat(balanceByCurrencyRepository.get(balanceByCurrency)).isEqualTo(balanceByCurrency);
    }

    public void thenABankTransactionShouldBeRegister(BankTransactionModel bankTransaction) {
        assertThat(bankTransactionRepository.get(bankTransaction)).isEqualTo(bankTransaction);
    }

    public void thenBankTransactionShouldHaveLengthOf(int size) {
        assertThat(bankTransactionRepository.getAll()).hasSize(size);
    }

    public void thenBalanceByCurrencyShouldHaveLengthOf(int size) {
        assertThat(balanceByCurrencyRepository.getAll()).hasSize(size);
    }
}

