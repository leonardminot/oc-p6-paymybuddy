package com.paymybuddy.utils;

import com.paymybuddy.domain.dto.BankAccountCreationCommandDTO;
import com.paymybuddy.domain.dto.BankTransactionCommandDTO;
import com.paymybuddy.domain.dto.UserRequestCommandDTO;
import com.paymybuddy.domain.model.*;
import com.paymybuddy.domain.repository.BankAccountRepository;
import com.paymybuddy.domain.repository.UserAccountRepository;
import com.paymybuddy.domain.repository.UserRelationRepository;
import com.paymybuddy.domain.service.BankAccountService;
import com.paymybuddy.domain.service.DateProvider;
import com.paymybuddy.domain.service.UserAccountService;
import com.paymybuddy.domain.service.UserRelationService;

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
    private final StubDateProvider dateProvider = new StubDateProvider();
    private UserAccountModel userAccountToCreate;
    private UserRequestCommandDTO userRequestCommandDTO;
    private final UserAccountService userAccountService = new UserAccountService(userAccountRepository);

    private final UserRelationService userRelationService = new UserRelationService(userRelationRepository, dateProvider);
    private final BankAccountService bankAccountService = new BankAccountService(bankAccountRepository, userAccountRepository);

    public void givenUserInDatabase(UserAccountModel userInDB) {
        userAccountRepository.save(userInDB);
    }
    public void givenNowIs(LocalDateTime now) {
        dateProvider.now = now;
    }

    public void givenBankAccountInDatabase(BankAccountModel bankAccount) {
        bankAccountRepository.save(bankAccount);
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

    public void whenRequestForCreateBankAccountAndThrow(BankAccountCreationCommandDTO bankAccountCreationCommandDTO, Exception exceptionToThrown) {
        assertThatThrownBy(() -> bankAccountService.create(bankAccountCreationCommandDTO))
                .isInstanceOf(exceptionToThrown.getClass())
                .hasMessageContaining(exceptionToThrown.getMessage());
    }

    public void whenCreateANewBankTransaction(BankTransactionCommandDTO bankTransactionCommand) {

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

    }

    public void thenABankTransactionShouldBeRegister(BankTransactionModel bankTransaction) {

    }
}

