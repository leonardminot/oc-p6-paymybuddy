package com.paymybuddy.integration.e2e;

import com.paymybuddy.integration.e2e.page.CreateAccountPage;
import com.paymybuddy.integration.e2e.page.LoginPage;
import com.paymybuddy.repository.jpa.*;
import com.paymybuddy.service.UserAccountService;
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

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("teste2e")
@Slf4j
public class UserAccountCreationE2EIT {
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
    private DeductionRepositoryJpa deductionRepositoryJpa;
    @Autowired
    private UserAccountService userAccountService;

    @BeforeAll
    static void beforeAll() {
        WebDriverManager.firefoxdriver().setup();
    }

    @BeforeEach
    void setUp() {
        webDriver = new FirefoxDriver();
        baseUrl = "http://localhost:" + port;
        deductionRepositoryJpa.deleteAll();
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
    void itShouldShouldCreateAnAccount() {
        // Given
        // When
        webDriver.get(baseUrl);
        final LoginPage loginPage = new LoginPage(webDriver);
        CreateAccountPage createAccountPage = loginPage.goToCreateAccount();
        createAccountPage.isCreateAccountDisplayed();
        LoginPage loginPageBack = createAccountPage.createAccount("Leo", "Minot", "leo@email.com", "LeoM", "Welcome123", "Welcome123");
        loginPageBack.isLoginPageDisplayed();

        // Then
        userAccountService.getUserWithEmail("leo@email.com").ifPresentOrElse(
                userAccount -> {
                    assertThat(userAccount.getUsername()).isEqualTo("LeoM");
                    assertThat(userAccount.getFirstName()).isEqualTo("Leo");
                    assertThat(userAccount.getLastName()).isEqualTo("Minot");
                },
                () -> Fail.fail("No User Account found.")
        );
    }

    @Test
    void itShouldShowAnErrorMessageWhenPasswordsAreDifferent() {
        // Given
        // When
        webDriver.get(baseUrl);
        final LoginPage loginPage = new LoginPage(webDriver);
        CreateAccountPage createAccountPage = loginPage.goToCreateAccount();
        createAccountPage.isCreateAccountDisplayed();
        createAccountPage.createAccount("Leo", "Minot", "leo@email.com", "LeoM", "Welcome123", "Welcome456");

        // Then
        assertThat(createAccountPage.isAlertDifferentPasswordDisplayed()).isTrue();
        assertThat(userAccountService.getUserWithEmail("leo@email.com")).isEmpty();

    }
}
