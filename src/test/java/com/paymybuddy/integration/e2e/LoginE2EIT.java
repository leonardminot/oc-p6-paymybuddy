package com.paymybuddy.integration.e2e;

import com.paymybuddy.dto.UserRequestCommandDTO;
import com.paymybuddy.integration.e2e.page.AddConnectionPage;
import com.paymybuddy.integration.e2e.page.HomePage;
import com.paymybuddy.integration.e2e.page.LoginPage;
import com.paymybuddy.integration.e2e.page.TransferPage;
import com.paymybuddy.model.UserAccount;
import com.paymybuddy.repository.jpa.*;
import com.paymybuddy.service.UserAccountService;
import com.paymybuddy.service.UserRelationService;
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
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("teste2e")
@Slf4j
public class LoginE2EIT {

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
    private BCryptPasswordEncoder passwordEncoder;
    @Autowired
    private UserAccountService userAccountService;
    @Autowired
    private UserRelationService userRelationService;

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
    void itShouldConnectToTheApplication() {
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
        log.info("User created: " + userAccountService.getUserWithEmail("leo@email.com").isPresent() + ", with email: " + userAccountService.getUserWithEmail("leo@email.com").get().getEmail());

        webDriver.get(baseUrl);
        final LoginPage loginPage = new LoginPage(webDriver);

        // When
        HomePage homePage = loginPage.login("leo@email.com", "Welcome123");

        // Then
        assertThat(homePage.isHomeDisplayed()).isTrue();
    }

    @Test
    void itShouldAddANewConnection() {
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

        UserAccount userToConnectTo = new UserAccount(
                "Victor",
                "Minot",
                "victor@email.com",
                passwordEncoder.encode("Welcome123"),
                "victorM",
                "USER"
        );
        userAccountService.createUserAccount(new UserRequestCommandDTO(
                userToConnectTo.getUsername(),
                userToConnectTo.getEmail(),
                userToConnectTo.getPassword(),
                userToConnectTo.getFirstName(),
                userToConnectTo.getLastName()
        ));
        // When
        webDriver.get(baseUrl);
        final LoginPage loginPage = new LoginPage(webDriver);

        HomePage homePage = loginPage.login("leo@email.com", "Welcome123");

        TransferPage transferPage = homePage.goToTransferPage();
        transferPage.isTransferPageDisplayed();

        AddConnectionPage addConnectionPage = transferPage.addConnection();
        AddConnectionPage addConnectionPageBack = addConnectionPage.addConnection("victorM");
        addConnectionPageBack.isConnectedUserCardDisplayed();

        // Then
        Optional<UserAccount> targetUser = userAccountService.getUserWithEmail("leo@email.com");
        Optional<UserAccount> expectedConnectedUser = userAccountService.getUserWithEmail("victor@email.com");
        List<UserAccount> relations;
        if (targetUser.isPresent() && expectedConnectedUser.isPresent()) {
            relations = userRelationService.getRelationsFor(targetUser.get());
            assertThat(relations).contains(expectedConnectedUser.get());
        } else {
            Fail.fail("No target user");
        }

    }
}
