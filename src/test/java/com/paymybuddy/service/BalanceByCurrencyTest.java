package com.paymybuddy.service;

import com.paymybuddy.model.BalanceByCurrency;
import com.paymybuddy.model.UserAccount;
import com.paymybuddy.utils.Fixture;
import com.paymybuddy.utils.UserAccountBuilder;
import org.junit.jupiter.api.*;

import java.util.List;
import java.util.UUID;

@Tag("UnitTest")
public class BalanceByCurrencyTest {

    @Nested
    @DisplayName("Feature: should fetch balance by currency")
    class ShouldFetchBalanceByCurrency {

        @Nested
        @DisplayName("Rule: fetch balance by currency for a given user")
        class ShouldFetchBalanceByCurrencyForGivenUser {
            private Fixture fixture;

            @BeforeEach
            void setUp() {
                fixture = new Fixture();
            }

            @Test
            void itShouldFetchBalanceByCurrency() {
                // Given
                UserAccount targetUser = new UserAccountBuilder()
                        .withId(UUID.fromString("1124d9e8-6266-4bcf-8035-37a02ba75c69"))
                        .withFirstName("Léo")
                        .withLastName("Minot")
                        .build();

                UserAccount otherUser = new UserAccountBuilder()
                        .withId(UUID.fromString("1124d9e8-0000-4bcf-8035-37a02ba75c69"))
                        .withFirstName("Victor")
                        .withLastName("Minot")
                        .build();

                fixture.givenUserInDatabase(targetUser);
                fixture.givenUserInDatabase(otherUser);

                BalanceByCurrency balanceByCurrency1 = new BalanceByCurrency(
                        UUID.fromString("1124d9e8-6266-4bcf-0000-37a02ba75c69"),
                        targetUser,
                        100.0,
                        "EUR"
                );

                BalanceByCurrency balanceByCurrency2 = new BalanceByCurrency(
                        UUID.fromString("1124d9e8-6266-4bcf-0001-37a02ba75c69"),
                        targetUser,
                        150.0,
                        "USD"
                );

                BalanceByCurrency balanceByCurrency3 = new BalanceByCurrency(
                        UUID.fromString("1124d9e8-6266-4bcf-0002-37a02ba75c69"),
                        otherUser,
                        100.0,
                        "EUR"
                );

                fixture.givenTheBalanceByCurrencyInDataBase(balanceByCurrency1);
                fixture.givenTheBalanceByCurrencyInDataBase(balanceByCurrency2);
                fixture.givenTheBalanceByCurrencyInDataBase(balanceByCurrency3);

                // When
                fixture.whenFetchBalanceByCurrencyFor(targetUser);

                // Then
                fixture.thenFetchBalanceByCurrencyShouldContain(List.of(balanceByCurrency1, balanceByCurrency2));

            }
        }
    }
}
