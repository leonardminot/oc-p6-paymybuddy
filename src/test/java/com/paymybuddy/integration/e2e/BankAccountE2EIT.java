package com.paymybuddy.integration.e2e;

import com.paymybuddy.dto.UserRequestCommandDTO;
import com.paymybuddy.integration.e2e.page.AddBankAccountPage;
import com.paymybuddy.integration.e2e.page.HomePage;
import com.paymybuddy.integration.e2e.page.LoginPage;
import com.paymybuddy.integration.e2e.page.ProfilePage;
import com.paymybuddy.model.BankAccount;
import com.paymybuddy.model.UserAccount;
import com.paymybuddy.repository.jpa.*;
import com.paymybuddy.service.BankAccountService;
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

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("teste2e")
@Slf4j
public class BankAccountE2EIT {
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
    private BankAccountService bankAccountService;
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
    void itShouldCreateANewBankAccount() {
        // Given
        UserAccount userToSave = new UserAccount(
                "Léo",
                "Minot",
                "leo@email.com",
                passwordEncoder.encode("Welcome123"),
                "leoM",
                "USER"
        );
        userAccountService.createUserAccount(new UserRequestCommandDTO(
                userToSave.getUsername(),
                userToSave.getEmail(),
                userToSave.getPassword(),
                userToSave.getFirstName(),
                userToSave.getLastName()
        ));

        // When
        webDriver.get(baseUrl);
        final LoginPage loginPage = new LoginPage(webDriver);

        HomePage homePage = loginPage.login("leo@email.com", "Welcome123");
        homePage.isHomeDisplayed();

        ProfilePage profilePage = homePage.goToProfilePage();
        profilePage.isProfilePageDisplayed();

        AddBankAccountPage addBankAccountPage = profilePage.addBankAccount();
        addBankAccountPage.isAddBankAccountPageDisplayed();

        ProfilePage profilePageBack = addBankAccountPage.createBankAccount("123456789", "FR");
        profilePageBack.isProfilePageDisplayed();

        // Then
        Optional<UserAccount> connectedUSer = userAccountService.getUserWithEmail("leo@email.com");
        List<BankAccount> actualAccounts = new ArrayList<>();
        if (connectedUSer.isPresent())
            actualAccounts = bankAccountService.getBankAccountsFor(connectedUSer.get());

        Optional<BankAccount> currentBankAccount = bankAccountRepositoryJpa.findByIban("123456789");

        if (currentBankAccount.isPresent()) {
            assertThat(actualAccounts.get(0).getIban()).isEqualTo(currentBankAccount.get().getIban());
            assertThat(actualAccounts.get(0).getCountry()).isEqualTo(currentBankAccount.get().getCountry());
            assertThat(actualAccounts.get(0).getUserAccount()).isEqualTo(currentBankAccount.get().getUserAccount());

        } else
            Fail.fail("No Bank account created for the user");
    }


}
