package com.paymybuddy.utils;

import com.paymybuddy.dto.*;
import com.paymybuddy.model.*;
import com.paymybuddy.repository.definition.*;
import com.paymybuddy.service.*;
import org.springframework.security.core.userdetails.User;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

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
    private final BankTransactionService bankTransactionService = new BankTransactionService(balanceByCurrencyService, bankAccountService, bankTransactionRepository, dateProvider);
    private final UserTransactionService userTransactionService = new UserTransactionService(balanceByCurrencyService, userTransactionRepository, userTransferRepository, dateProvider);
    List<UserAccount> connectedUser = new ArrayList<>();

    Optional<UserAccount> actualUser = Optional.empty();
    List<BankAccount> bankAccounts = new ArrayList<>();
    Optional<BankAccount> currentBankAccount = Optional.empty();
    List<BankTransaction> actualBankTransactions = new ArrayList<>();
    List<BalanceByCurrency> actualBalanceByCurrency = new ArrayList<>();
    List<UserTransactionDTO> actualUserTransaction = new ArrayList<>();

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
    public void givenTheUserTransactionInDatabase(Transaction transaction) {
        userTransactionRepository.save(transaction);
    }

    public void givenTheUserTransferInDatabase(Transfer transfer) {
        userTransferRepository.save(transfer);
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

    public void givenAConnectionExistsBetween(UserAccount user1, UserAccount user2) {
        userRelationService.createRelation(user1, user2);
    }

    public void whenRequestAUserInDatabaseWithEmail(String mail) {
        actualUser = userAccountService.getUserWithEmail(mail);
    }
    public void whenRequestACreationOfARelationBetween(UserAccount user1, UserAccount user2) {
        userRelationService.createRelation(user1, user2);
    }

    public void whenRequestConnectedUserFor(UserAccount principalUser) {
        connectedUser = userRelationService.getRelationsFor(principalUser);
    }

    public void whenRequestACreationOfARelationBetweenThenThrow(UserAccount user1, UserAccount user2, Exception exception) {
        assertThatThrownBy(() -> userRelationService.createRelation(user1, user2))
                .isInstanceOf(exception.getClass())
                .hasMessageContaining(exception.getMessage());
    }

    public void whenRequestForCreateBankAccountAndThenThrow(BankAccountCreationCommandDTO bankAccountCreationCommandDTO, Exception exceptionToThrown) {
        assertThatThrownBy(() -> bankAccountService.create(bankAccountCreationCommandDTO))
                .isInstanceOf(exceptionToThrown.getClass())
                .hasMessageContaining(exceptionToThrown.getMessage());
    }

    public void whenFetchBankAccountWithId(UUID bankAccountId) {
        currentBankAccount = bankAccountService.getBankAccount(bankAccountId);
    }

    public void whenCreateANewBankTransaction(BankTransactionCommandDTO bankTransactionCommand) {
        bankTransactionService.newTransaction(bankTransactionCommand);
    }
    public void whenCreateABankTransactionAndThenThrow(BankTransactionCommandDTO bankTransactionCommand, Exception exceptionToThrown) {
        assertThatThrownBy(() -> bankTransactionService.newTransaction(bankTransactionCommand))
                .isInstanceOf(exceptionToThrown.getClass())
                .hasMessageContaining(exceptionToThrown.getMessage());
    }
    public void whenCreateANewBankTransactionThenThrow(BankTransactionCommandDTO bankTransactionCommand, Exception exceptionToThrow) {
        assertThatThrownBy(() -> bankTransactionService.newTransaction(bankTransactionCommand))
                .isInstanceOf(exceptionToThrow.getClass())
                .hasMessageContaining(exceptionToThrow.getMessage());
    }
    public void whenCreateATransactionBetweenUsers(UserAccount fromUser, UserAccount toUser, String description, String currency, double amount) {
        userTransactionService.performTransaction(new UserTransactionCommand(fromUser, toUser, description, currency,amount));
    }
    public void whenCreateATransactionBetweenUsersAndThenThrow(UserAccount fromUser, UserAccount toUser, String description, String currency, double amount, Exception exceptionThrown) {
        assertThatThrownBy(() -> userTransactionService.performTransaction(new UserTransactionCommand(fromUser, toUser, description, currency, amount)))
                .isInstanceOf(exceptionThrown.getClass())
                .hasMessageContaining(exceptionThrown.getMessage());
    }

    public void whenFetchAllUsers() {
        connectedUser = userAccountService.getAllUsers();
    }

    public void whenFetchAllBankAccountsForUser(UserAccount user) {
        bankAccounts = bankAccountService.getBankAccountsFor(user);
    }

    public void whenFetchBankTransactionFor(UserAccount targetUser) {
        actualBankTransactions = bankTransactionService.fetchTransactionsFor(targetUser);
    }
    public void whenFetchBalanceByCurrencyFor(UserAccount targetUser) {
        actualBalanceByCurrency = balanceByCurrencyService.fetchBalanceByCurrencyFor(targetUser);
    }

    public void whenFetchUserTransactionFor(UserAccount targetUser) {
        actualUserTransaction = userTransactionService.getTransactionsFor(targetUser);
    }

    public void thenTheUserShouldBeAndSaved(UserAccount expectedUserAccount) {
        assertThat(userAccountRepository.get(userAccountToCreate).get()).isEqualTo(expectedUserAccount);
    }

    public void thenItShouldThrowAnException(Exception e) {
        assertThatThrownBy(() -> userAccountService.createUserAccount(this.userRequestCommandDTO))
                .isInstanceOf(e.getClass())
                .hasMessageContaining(e.getMessage());
    }

    public void thenReturnedUserShouldBe(Optional<UserAccount> expectedUser) {
        assertThat(actualUser).isEqualTo(expectedUser);
    }

    public void thenUsersShouldBe(List<UserAccount> expectedUsers) {
        assertThat(connectedUser).containsAll(expectedUsers);
    }

    public void thenARelationHasToBeCreateAndEqualTo(Relation expectedRelation) {
        assertThat(userRelationRepository.getRelation(expectedRelation.getUser1(), expectedRelation.getUser2())).isEqualTo(expectedRelation);
    }

    public void thenConnectedUserListShouldBe(List<UserAccount> expectedConnectedUsers) {
        assertThat(connectedUser).containsAll(expectedConnectedUsers);
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

    public void thenItShouldTheReturnListOfBankAccounts(List<BankAccount> expectedBankAccounts) {
        assertThat(bankAccounts).hasSameElementsAs(expectedBankAccounts);
    }

    public void thenItShouldReturnTheBankAccount(BankAccount bankAccount) {
        assertThat(currentBankAccount).isPresent();
        assertThat(currentBankAccount.get()).isEqualTo(bankAccount);
    }

    public void thenTransactionListShouldContain(List<BankTransaction> expectedBankTransaction) {
        assertThat(actualBankTransactions).hasSameElementsAs(expectedBankTransaction);
    }

    public void thenFetchBalanceByCurrencyShouldContain(List<BalanceByCurrency> expectedBalanceByCurrency) {
        assertThat(actualBalanceByCurrency).hasSameElementsAs(expectedBalanceByCurrency);
    }

    public void thenTransactionListShouldBe(List<UserTransactionDTO> expectedTransactions) {
        assertThat(actualUserTransaction).hasSameElementsAs(expectedTransactions);
    }
}

