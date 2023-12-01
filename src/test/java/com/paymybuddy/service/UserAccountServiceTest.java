package com.paymybuddy.service;

import com.paymybuddy.application.model.UserAccount;
import com.paymybuddy.coremodel.dto.UserRequestCommandDTO;
import com.paymybuddy.coremodel.model.UserAccountModel;
import com.paymybuddy.coremodel.model.UserRelationModel;
import com.paymybuddy.coremodel.repository.UserRelationRepository;
import com.paymybuddy.coremodel.service.DateProvider;
import com.paymybuddy.coremodel.service.UserAccountService;
import com.paymybuddy.coremodel.service.UserRelationService;
import com.paymybuddy.utils.StubUserAccountRepository;
import com.paymybuddy.utils.StubUserRelationRepository;
import org.junit.jupiter.api.*;

import java.time.LocalDateTime;
import java.util.UUID;

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
                fixture.givenTheFollowingEmailIsInDatabase();
                // When
                fixture.whenArequestForCreateUserThatThrow(new UserRequestCommandDTO("LeoM", "leo@email.com", "123", "Léo", "Minot"));
                // Then
                fixture.thenItShouldThrowAnException(new RuntimeException("Email already exists."));
            }
        }

        @Nested
        @DisplayName("Rule: username should be unique")
        class UsernameShouldBeUnique {
            private Fixture fixture;
            @BeforeEach
            void setUp() {
                fixture = new Fixture();
            }

            @Test
            void itShouldThrowIfUsernameIsAlreadyInDatabase() {
                // Given
                fixture.givenTheFollowingUserNameIsInDatabase();
                // When
                fixture.whenArequestForCreateUserThatThrow(new UserRequestCommandDTO("LeoM", "leo@email.com", "123", "Léo", "Minot"));
                // Then
                fixture.thenItShouldThrowAnException(new RuntimeException("UserName already exists."));
            }
        }

    }

    @Nested
    @DisplayName("Feature: create a relation between two users")
    class CreateRelationTest {
        @Nested
        @DisplayName("Rule: No Fields should be empty")
        class NoFieldsEmpty {
            private Fixture fixture;
            @BeforeEach
            void setUp() {
                fixture = new Fixture();
            }

            @Test
            void itShouldCreateANewRelation() {
                // Given
                UserAccountModel leo = new UserAccountModel(
                        UUID.fromString("1124d9e8-6266-4bcf-8035-37a02ba75c69"),
                        "Léo",
                        "Minot",
                        "leo@email.com",
                        "AZERTY",
                        "LeoM"
                );
                UserAccountModel victor = new UserAccountModel(
                        UUID.fromString("2124d9e8-6266-4bcf-8035-37a02ba75c69"),
                        "Victor",
                        "Minot",
                        "victor@email.com",
                        "AZERTY",
                        "VictorM"
                );
                fixture.givenNowIs(LocalDateTime.of(2013, 12, 1, 15, 42, 0, 0));
                // When
                fixture.whenRequestACreationOfARelationBetween(leo, victor);
                // Then
                fixture.thenARelationHasToBeCreate(new UserRelationModel(leo, victor, LocalDateTime.of(2013, 12, 1, 15, 42, 0, 0)));
            }

            @Test
            void itShouldThrowIfUser1IsEmpty() {
                // Given
                UserAccountModel leo = new UserAccountModel(
                        UUID.fromString("1124d9e8-6266-4bcf-8035-37a02ba75c69"),
                        "Léo",
                        "Minot",
                        "leo@email.com",
                        "AZERTY",
                        "LeoM"
                );
                // When
                // Then
                fixture.whenRequestACreationOfARelationBetweenThenThrow(null, leo, "Users should not be empty");
            }

            @Test
            void itShouldThrowIfUser2IsEmpty() {
                // Given
                UserAccountModel leo = new UserAccountModel(
                        UUID.fromString("1124d9e8-6266-4bcf-8035-37a02ba75c69"),
                        "Léo",
                        "Minot",
                        "leo@email.com",
                        "AZERTY",
                        "LeoM"
                );
                // When
                // Then
                fixture.whenRequestACreationOfARelationBetweenThenThrow(leo, null, "Users should not be empty");
            }
        }

        @Nested
        @DisplayName("Rule: both users should be different")
        class UsersShouldBeDifferent {
            private Fixture fixture;
            @BeforeEach
            void setUp() {
                fixture = new Fixture();
            }
            @Test
            void itShouldThrowIfIntentToCreateARelationWithTheSameUser() {
                // Given
                UserAccountModel leo = new UserAccountModel(
                        UUID.fromString("1124d9e8-6266-4bcf-8035-37a02ba75c69"),
                        "Léo",
                        "Minot",
                        "leo@email.com",
                        "AZERTY",
                        "LeoM"
                );
                // When
                // Then
                fixture.whenRequestACreationOfARelationBetweenThenThrow(leo, leo, "Users must be different");
            }
        }

        @Nested
        @DisplayName("Rule: In a relation, users should be ordered by Id")
        class OrderedUsersById {
            private Fixture fixture;
            @BeforeEach
            void setUp() {
                fixture = new Fixture();
            }

            @Test
            void itShouldOrderedTheRelationByUUID() {
                // Given
                UserAccountModel leo = new UserAccountModel(
                        UUID.fromString("1124d9e8-6266-4bcf-8035-37a02ba75c69"),
                        "Léo",
                        "Minot",
                        "leo@email.com",
                        "AZERTY",
                        "LeoM"
                );
                UserAccountModel victor = new UserAccountModel(
                        UUID.fromString("2124d9e8-6266-4bcf-8035-37a02ba75c69"),
                        "Victor",
                        "Minot",
                        "victor@email.com",
                        "AZERTY",
                        "VictorM"
                );
                fixture.givenNowIs(LocalDateTime.of(2013, 12, 1, 15, 42, 0, 0));
                // When
                fixture.whenRequestACreationOfARelationBetween(victor, leo); // Victor has a UUID superior in the alphabetical order. At the end, Leo should be first
                // Then
                fixture.thenARelationHasToBeCreate(new UserRelationModel(leo, victor, LocalDateTime.of(2013, 12, 1, 15, 42, 0, 0)));
            }
        }
    }


    class StubDateProvider implements DateProvider {
        LocalDateTime now;
        @Override
        public LocalDateTime getNow() {
            return now;
        }
    }

    class Fixture {
        private final StubUserAccountRepository userAccountRepository = new StubUserAccountRepository();

        private final UserRelationRepository userRelationRepository = new StubUserRelationRepository();
        private final StubDateProvider dateProvider = new StubDateProvider();

        private UserAccount userAccountToCreate;
        private UserRequestCommandDTO userRequestCommandDTO;

        private final UserAccountService userAccountService = new UserAccountService(userAccountRepository);
        private final UserRelationService userRelationService = new UserRelationService(userRelationRepository, dateProvider);

        public void givenTheFollowingEmailIsInDatabase() {
            userAccountRepository.setAnExistingEmail(true);
        }

        public void givenTheFollowingUserNameIsInDatabase() {
            userAccountRepository.setAnExistingUserName(true);
        }

        public void givenNowIs(LocalDateTime now) {
            dateProvider.now = now;
        }

        public void whenArequestForCreateUser(UserRequestCommandDTO userRequestCommandDTO) {
            userAccountToCreate = userAccountService.createUserAccount(userRequestCommandDTO);
        }

        public void whenArequestForCreateUserThatThrow(UserRequestCommandDTO userRequestCommandDTO) {
            this.userRequestCommandDTO = userRequestCommandDTO;
        }

        public void whenRequestACreationOfARelationBetween(UserAccountModel user1, UserAccountModel user2) {
            userRelationService.createRelation(user1, user2);
        }

        public void thenTheUserShouldBeAndSaved(UserAccount expectedUserAccount) {
            assertThat(userAccountToCreate).isEqualTo(expectedUserAccount);
            assertThat(userAccountRepository.get(userAccountToCreate)).isEqualTo(expectedUserAccount);
        }

        public void thenItShouldThrowAnException(RuntimeException e) {
            assertThatThrownBy(() -> userAccountService.createUserAccount(this.userRequestCommandDTO))
                    .isInstanceOf(RuntimeException.class)
                    .hasMessageContaining(e.getMessage());
        }

        public void thenARelationHasToBeCreate(UserRelationModel expectedRelation) {
            assertThat(userRelationRepository.getRelation(expectedRelation.user1(), expectedRelation.user2())).isEqualTo(expectedRelation);
        }

        public void whenRequestACreationOfARelationBetweenThenThrow(UserAccountModel user1, UserAccountModel user2, String message) {
            assertThatThrownBy(() -> userRelationService.createRelation(user1, user2))
                    .isInstanceOf(RuntimeException.class)
                    .hasMessageContaining(message);
        }
    }

}