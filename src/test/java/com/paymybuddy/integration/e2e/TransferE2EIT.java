package com.paymybuddy.integration.e2e;

import com.paymybuddy.dto.BankAccountCreationCommandDTO;
import com.paymybuddy.dto.BankTransactionCommandDTO;
import com.paymybuddy.dto.UserRequestCommandDTO;
import com.paymybuddy.dto.UserTransactionDTO;
import com.paymybuddy.integration.e2e.page.HomePage;
import com.paymybuddy.integration.e2e.page.LoginPage;
import com.paymybuddy.integration.e2e.page.TransferPage;
import com.paymybuddy.model.BalanceByCurrency;
import com.paymybuddy.model.BankAccount;
import com.paymybuddy.model.Currency;
import com.paymybuddy.model.UserAccount;
import com.paymybuddy.repository.jpa.*;
import com.paymybuddy.service.*;
import io.github.bonigarcia.wdm.WebDriverManager;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Fail;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("teste2e")
@Slf4j
public class TransferE2EIT {
    @LocalServerPort
    private Integer port;
    private WebDriver webDriver = null;
    private String baseUrl;

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
    @Autowired
    private BCryptPasswordEncoder passwordEncoder;
    @Autowired
    private UserAccountService userAccountService;
    @Autowired
    private UserRelationService userRelationService;
    @Autowired
    private BankAccountService bankAccountService;
    @Autowired
    private BankTransactionService bankTransactionService;
    @Autowired
    private BalanceByCurrencyService balanceByCurrencyService;
    @Autowired
    private UserTransactionService userTransactionService;

    @BeforeAll
    static void beforeAll() {
        WebDriverManager.firefoxdriver().setup();
    }

    @BeforeEach
    void setUp() {
        webDriver = new FirefoxDriver();
        baseUrl = "http://localhost:" + port;
        transferRepositoryJpa.deleteAll();
        transactionRepositoryJpa.deleteAll();
        relationRepositoryJpa.deleteAll();
        bankTransactionRepositoryJpa.deleteAll();
        bankAccountRepositoryJpa.deleteAll();
        balanceByCurrencyRepositoryJpa.deleteAll();
        userAccountRepositoryJpa.deleteAll();
    }

    @AfterEach
    void tearDown() {
        if (webDriver != null) {
            webDriver.quit();
        }
    }

    @Test
    void itShouldTransferMoneyBetweenUsers() {
        // Given
        UserAccount fromUser = new UserAccount(
                "Léo",
                "Minot",
                "leo@email.com",
                passwordEncoder.encode("Welcome123"),
                "leoM",
                "USER"
        );
        fromUser = userAccountService.createUserAccount(new UserRequestCommandDTO(
                fromUser.getUsername(),
                fromUser.getEmail(),
                fromUser.getPassword(),
                fromUser.getFirstName(),
                fromUser.getLastName()
        ));

        UserAccount toUser = new UserAccount(
                "Victor",
                "Minot",
                "victor@email.com",
                passwordEncoder.encode("Welcome123"),
                "victorM",
                "USER"
        );

        toUser = userAccountService.createUserAccount(new UserRequestCommandDTO(
                toUser.getUsername(),
                toUser.getEmail(),
                toUser.getPassword(),
                toUser.getFirstName(),
                toUser.getLastName()
        ));

        userRelationService.createRelation(fromUser, toUser);

        BankAccount bankAccountForFromUser = bankAccountService.create(new BankAccountCreationCommandDTO(
                fromUser,
                "123456789",
                "FR"
        ));

        bankTransactionService.newTransaction(new BankTransactionCommandDTO(
                bankAccountForFromUser,
                100.0,
                Currency.EUR
        ));


        // When
        webDriver.get(baseUrl);
        final LoginPage loginPage = new LoginPage(webDriver);
        HomePage homePage = loginPage.login("leo@email.com", "Welcome123");
        TransferPage transferPage = homePage.goToTransferPage();
        transferPage.isTransferPageDisplayed();
        TransferPage transferPageBack = transferPage.transferMoneyToAnUser(toUser.getUsername(), 90.0, Currency.EUR, "Test e2e description");
        transferPageBack.isTransferPageDisplayed();

        // Then
        List<BalanceByCurrency> balanceByCurrenciesForFromUser = balanceByCurrencyService.fetchBalanceByCurrencyFor(fromUser);
        balanceByCurrenciesForFromUser.stream()
                .filter(bbc -> bbc.getCurrency().equals(Currency.EUR))
                .findFirst().ifPresentOrElse(
                        bbc -> assertThat(bbc.getBalance()).isEqualTo(10.0),
                        () -> Fail.fail("No balance by Currency for From User")
                );

        List<BalanceByCurrency> balanceByCurrenciesForToUser = balanceByCurrencyService.fetchBalanceByCurrencyFor(toUser);
        balanceByCurrenciesForToUser.stream()
                .filter(bbc -> bbc.getCurrency().equals(Currency.EUR))
                .findFirst().ifPresentOrElse(
                        bbc -> assertThat(bbc.getBalance()).isEqualTo(90.0 * 0.995),
                        () -> Fail.fail("No balance by Currency for To User")
                );

        List<UserTransactionDTO> transactionsForToUser = userTransactionService.getTransactionsFor(toUser);
        transactionsForToUser.stream()
                .filter(transaction -> transaction.from().getEmail().equals("leo@email.com"))
                .findFirst()
                .ifPresentOrElse(
                        transaction -> assertThat(transaction.amount()).isEqualTo(90.0 * 0.995),
                        () -> Fail.fail("No transactions found")
                );
    }

    @Test
    void itShouldDisplayAnErrorMessage() {
        // Given
        UserAccount fromUser = new UserAccount(
                "Léo",
                "Minot",
                "leo@email.com",
                passwordEncoder.encode("Welcome123"),
                "leoM",
                "USER"
        );
        fromUser = userAccountService.createUserAccount(new UserRequestCommandDTO(
                fromUser.getUsername(),
                fromUser.getEmail(),
                fromUser.getPassword(),
                fromUser.getFirstName(),
                fromUser.getLastName()
        ));

        UserAccount toUser = new UserAccount(
                "Victor",
                "Minot",
                "victor@email.com",
                passwordEncoder.encode("Welcome123"),
                "victorM",
                "USER"
        );

        toUser = userAccountService.createUserAccount(new UserRequestCommandDTO(
                toUser.getUsername(),
                toUser.getEmail(),
                toUser.getPassword(),
                toUser.getFirstName(),
                toUser.getLastName()
        ));

        userRelationService.createRelation(fromUser, toUser);

        BankAccount bankAccountForFromUser = bankAccountService.create(new BankAccountCreationCommandDTO(
                fromUser,
                "123456789",
                "FR"
        ));

        bankTransactionService.newTransaction(new BankTransactionCommandDTO(
                bankAccountForFromUser,
                100.0,
                Currency.EUR
        ));

        // When
        webDriver.get(baseUrl);
        final LoginPage loginPage = new LoginPage(webDriver);
        HomePage homePage = loginPage.login("leo@email.com", "Welcome123");
        TransferPage transferPage = homePage.goToTransferPage();
        transferPage.isTransferPageDisplayed();
        TransferPage transferPageBack = transferPage.transferMoneyToAnUser(toUser.getUsername(), 200.0, Currency.EUR, "Test e2e description");
        transferPageBack.isTransferPageDisplayed();

        // Then
        assertThat(transferPageBack.isNotEnoughMoneyAlertDisplayed()).isTrue();
    }
}
