package com.paymybuddy.integration;

import com.paymybuddy.application.model.UserAccount;
import com.paymybuddy.application.repository.UserAccountRepositoryJpa;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
public class RepositoryJPAIT {

    @Autowired
    private UserAccountRepositoryJpa userAccountRepositoryJpa;

    @BeforeEach
    void setUp() {
        userAccountRepositoryJpa.deleteAll();
    }

    @Test
    void itShouldSaveANewUserAccount() {
        // Given
        UserAccount userToSave = new UserAccount(
                "Léo",
                "Minot",
                "leo@email.com",
                "Welcome123",
                "leoM"
        );

        // When
        userAccountRepositoryJpa.save(userToSave);

        // Then
        Iterable<UserAccount> userAccounts = userAccountRepositoryJpa.findAll();
        assertThat(userToSave).isIn(userAccounts);

    }
}
