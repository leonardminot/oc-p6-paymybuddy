package com.paymybuddy.service;

import com.paymybuddy.dto.UserRequestCommandDTO;
import com.paymybuddy.model.UserAccount;
import com.paymybuddy.repository.UserAccountRepositoryJpa;
import org.junit.jupiter.api.*;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@Tag("UnitTest")
class UserAccountServiceTest {

    @Nested
    @DisplayName("Feature: create a user account")
    class CreateUserAccountTest {
        @Nested
        @DisplayName("Rule: no fields should be blank")
        class NoFieldShouldBeBlankTest {
            private Fixture fixture;

            @BeforeEach
            void setUp() {
               fixture = new Fixture();
            }

            @Test
            void itShouldCreateANewUser() {
                // When
                fixture.whenArequestForCreateUser(new UserRequestCommandDTO("LeoM", "leo@email.com", "123", "Léo", "Minot"));

                // Then
                fixture.thenTheUserShouldBeAndSaved(new UserAccount("Léo", "Minot", "leo@email.com", "123", "LeoM"));
            }

            @Test
            void itShouldThrowWhenUserNameIsEmpty() {
                // Given
                // When
                fixture.whenArequestForCreateUserThatThrow(new UserRequestCommandDTO("", "leo@email.com", "123", "Léo", "Minot"));

                // Then
                fixture.thenItShouldThrowAnException(new RuntimeException("Username shouldn't be empty."));
            }

            @Test
            void itShouldThrowWhenFirstNameIsEmpty() {
                // Given
                // When
                fixture.whenArequestForCreateUserThatThrow(new UserRequestCommandDTO("LeoM", "leo@email.com", "123", "", "Minot"));

                // Then
                fixture.thenItShouldThrowAnException(new RuntimeException("Firstname shouldn't be empty."));
            }

            @Test
            void itShouldThrowWhenLastNameIsEmpty() {
                // Given
                // When
                fixture.whenArequestForCreateUserThatThrow(new UserRequestCommandDTO("LeoM", "leo@email.com", "123", "Léo", ""));

                // Then
                fixture.thenItShouldThrowAnException(new RuntimeException("Lastname shouldn't be empty."));
            }

            @Test
            void itShouldThrowWhenPasswordIsEmpty() {
                // Given
                // When
                fixture.whenArequestForCreateUserThatThrow(new UserRequestCommandDTO("LeoM", "leo@email.com", "", "Léo", "Minot"));

                // Then
                fixture.thenItShouldThrowAnException(new RuntimeException("Password shouldn't be empty."));
            }

            @Test
            void itShouldThrowWhenEmailIsEmpty() {
                // Given
                // When
                fixture.whenArequestForCreateUserThatThrow(new UserRequestCommandDTO("LeoM", "", "123", "Léo", "Minot"));

                // Then
                fixture.thenItShouldThrowAnException(new RuntimeException("Email shouldn't be empty."));
            }
        }

        @Nested
        @DisplayName("Rule: email should be unique")
        class EmailShouldBeUnique {
            private Fixture fixture;

            @BeforeEach
            void setUp() {
                fixture = new Fixture();
            }

            @Test
            void itShouldThrowIlTheSameEmailIsAlreadyInDatabase() {
                // Given
                fixture.givenTheFollowingEmailIsInDatabase("leo@email.com");
                // When
                fixture.whenArequestForCreateUser(new UserRequestCommandDTO("LeoM", "leo@email.com", "123", "Léo", "Minot"));

                // Then
                fixture.thenItShouldThrowAnException(new RuntimeException("Email already exists."));
            }
        }

    }


    class Fixture {
        private final UserAccountRepositoryJpa userAccountRepositoryJpa = Mockito.mock(UserAccountRepositoryJpa.class);

        private final ArgumentCaptor<UserAccount> userAccountArgumentCaptor = ArgumentCaptor.forClass(UserAccount.class);;
        private UserAccount userAccountToCreate;
        private UserRequestCommandDTO userRequestCommandDTO;

        private final UserAccountService userAccountService = new UserAccountService(userAccountRepositoryJpa);

        public void givenTheFollowingEmailIsInDatabase(String mail) {
            // TODO("Encore en encore travailler le mocking")
        }

        public void whenArequestForCreateUser(UserRequestCommandDTO userRequestCommandDTO) {
            userAccountToCreate = userAccountService.createUserAccount(userRequestCommandDTO);
        }

        public void whenArequestForCreateUserThatThrow(UserRequestCommandDTO userRequestCommandDTO) {
            this.userRequestCommandDTO = userRequestCommandDTO;
        }

        public void thenTheUserShouldBeAndSaved(UserAccount expectedUserAccount) {
            assertThat(userAccountToCreate).isEqualTo(expectedUserAccount);
            Mockito.verify(userAccountRepositoryJpa).save(userAccountArgumentCaptor.capture());
            assertThat(userAccountArgumentCaptor.getValue()).isEqualTo(expectedUserAccount);
        }

        public void thenItShouldThrowAnException(RuntimeException e) {
            assertThatThrownBy(() -> userAccountService.createUserAccount(this.userRequestCommandDTO))
                    .isInstanceOf(RuntimeException.class)
                    .hasMessageContaining(e.getMessage());
        }
    }

}