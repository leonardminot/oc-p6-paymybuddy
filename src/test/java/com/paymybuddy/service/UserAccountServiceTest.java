package com.paymybuddy.service;

import com.paymybuddy.coremodel.dto.UserRequestCommandDTO;
import com.paymybuddy.coremodel.model.UserAccountModel;
import com.paymybuddy.coremodel.model.UserRelationModel;
import com.paymybuddy.coremodel.repository.UserAccountRepository;
import com.paymybuddy.coremodel.repository.UserRelationRepository;
import com.paymybuddy.coremodel.service.DateProvider;
import com.paymybuddy.coremodel.service.UserAccountService;
import com.paymybuddy.coremodel.service.UserRelationService;
import com.paymybuddy.utils.FakeUserAccountRepository;
import com.paymybuddy.utils.FakeUserRelationRepository;
import com.paymybuddy.utils.UserAccountBuilder;
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
                fixture.whenRequestForCreateUser(new UserRequestCommandDTO("LeoM", "leo@email.com", "123", "Léo", "Minot"));

                // Then
                fixture.thenTheUserShouldBeAndSaved(new UserAccountModel(null,"Léo", "Minot", "leo@email.com", "123", "LeoM"));
            }

            @Test
            void itShouldThrowWhenUserNameIsEmpty() {
                // Given
                // When
                fixture.whenRequestForCreateUserThatThrow(new UserRequestCommandDTO("", "leo@email.com", "123", "Léo", "Minot"));

                // Then
                fixture.thenItShouldThrowAnException(new RuntimeException("Username shouldn't be empty."));
            }

            @Test
            void itShouldThrowWhenFirstNameIsEmpty() {
                // Given
                // When
                fixture.whenRequestForCreateUserThatThrow(new UserRequestCommandDTO("LeoM", "leo@email.com", "123", "", "Minot"));

                // Then
                fixture.thenItShouldThrowAnException(new RuntimeException("Firstname shouldn't be empty."));
            }

            @Test
            void itShouldThrowWhenLastNameIsEmpty() {
                // Given
                // When
                fixture.whenRequestForCreateUserThatThrow(new UserRequestCommandDTO("LeoM", "leo@email.com", "123", "Léo", ""));

                // Then
                fixture.thenItShouldThrowAnException(new RuntimeException("Lastname shouldn't be empty."));
            }

            @Test
            void itShouldThrowWhenPasswordIsEmpty() {
                // Given
                // When
                fixture.whenRequestForCreateUserThatThrow(new UserRequestCommandDTO("LeoM", "leo@email.com", "", "Léo", "Minot"));

                // Then
                fixture.thenItShouldThrowAnException(new RuntimeException("Password shouldn't be empty."));
            }

            @Test
            void itShouldThrowWhenEmailIsEmpty() {
                // Given
                // When
                fixture.whenRequestForCreateUserThatThrow(new UserRequestCommandDTO("LeoM", "", "123", "Léo", "Minot"));

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
                UserAccountModel userInDB = new UserAccountBuilder().withEmail("leo@email.com").build();
                fixture.givenUserInDatabase(userInDB);
                // When
                fixture.whenRequestForCreateUserThatThrow(new UserRequestCommandDTO("LeoM", "leo@email.com", "123", "Léo", "Minot"));
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
                UserAccountModel userInDB = new UserAccountBuilder().withUsername("LeoM").build();
                fixture.givenUserInDatabase(userInDB);
                // When
                fixture.whenRequestForCreateUserThatThrow(new UserRequestCommandDTO("LeoM", "leo@email.com", "123", "Léo", "Minot"));
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
                UserAccountModel leo = new UserAccountBuilder().withId(UUID.fromString("1124d9e8-6266-4bcf-8035-37a02ba75c69")).withFirstName("Léo").withUsername("LeoM").build();
                UserAccountModel victor = new UserAccountBuilder().withId(UUID.fromString("2124d9e8-6266-4bcf-8035-37a02ba75c69")).withFirstName("Victor").withUsername("VictorM").build();

                fixture.givenNowIs(LocalDateTime.of(2013, 12, 1, 15, 42, 0, 0));
                // When
                fixture.whenRequestACreationOfARelationBetween(leo, victor);
                // Then
                fixture.thenARelationHasToBeCreateAndEqualTo(new UserRelationModel(leo, victor, LocalDateTime.of(2013, 12, 1, 15, 42, 0, 0)));
            }

            @Test
            void itShouldThrowIfUser1IsEmpty() {
                // Given
                UserAccountModel user = new UserAccountBuilder().build();
                // When
                // Then
                fixture.whenRequestACreationOfARelationBetweenThenThrow(null, user, "Users should not be empty");
            }

            @Test
            void itShouldThrowIfUser2IsEmpty() {
                // Given
                UserAccountModel user = new UserAccountBuilder().build();
                // When
                // Then
                fixture.whenRequestACreationOfARelationBetweenThenThrow(user, null, "Users should not be empty");
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
                UserAccountModel user = new UserAccountBuilder().build();
                // When
                // Then
                fixture.whenRequestACreationOfARelationBetweenThenThrow(user, user, "Users must be different");
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
                UserAccountModel userWithSmallestID = new UserAccountBuilder()
                        .withId(UUID.fromString("1124d9e8-6266-4bcf-8035-37a02ba75c69"))
                        .withFirstName("Léo")
                        .withLastName("Minot")
                        .build();
//
                UserAccountModel userWithHighestId = new UserAccountBuilder()
                        .withId(UUID.fromString("2124d9e8-6266-4bcf-8035-37a02ba75c69"))
                        .withFirstName("Victor")
                        .withLastName("Minot")
                        .build();

                LocalDateTime now = LocalDateTime.of(2013, 12, 1, 15, 42, 0, 0);
                fixture.givenNowIs(now);
                // When
                fixture.whenRequestACreationOfARelationBetween(userWithHighestId, userWithSmallestID); // Victor has a UUID superior in the alphabetical order. At the end, Leo should be first
                // Then
                fixture.thenARelationHasToBeCreateAndEqualTo(new UserRelationModel(userWithSmallestID, userWithHighestId, now));
            }
        }
    }


    static class StubDateProvider implements DateProvider {
        LocalDateTime now;
        @Override
        public LocalDateTime getNow() {
            return now;
        }
    }

    class Fixture {
        private final UserAccountRepository userAccountRepository = new FakeUserAccountRepository();

        private final UserRelationRepository userRelationRepository = new FakeUserRelationRepository();
        private final StubDateProvider dateProvider = new StubDateProvider();

        private UserAccountModel userAccountToCreate;
        private UserRequestCommandDTO userRequestCommandDTO;

        private final UserAccountService userAccountService = new UserAccountService(userAccountRepository);
        private final UserRelationService userRelationService = new UserRelationService(userRelationRepository, dateProvider);


        public void givenUserInDatabase(UserAccountModel userInDB) {
            userAccountRepository.save(userInDB);
        }

        public void givenNowIs(LocalDateTime now) {
            dateProvider.now = now;
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

        public void thenTheUserShouldBeAndSaved(UserAccountModel expectedUserAccount) {
            assertThat(userAccountRepository.get(userAccountToCreate)).isEqualTo(expectedUserAccount);
        }

        public void thenItShouldThrowAnException(RuntimeException e) {
            assertThatThrownBy(() -> userAccountService.createUserAccount(this.userRequestCommandDTO))
                    .isInstanceOf(RuntimeException.class)
                    .hasMessageContaining(e.getMessage());
        }

        public void thenARelationHasToBeCreateAndEqualTo(UserRelationModel expectedRelation) {
            assertThat(userRelationRepository.getRelation(expectedRelation.user1(), expectedRelation.user2())).isEqualTo(expectedRelation);
        }

        public void whenRequestACreationOfARelationBetweenThenThrow(UserAccountModel user1, UserAccountModel user2, String message) {
            assertThatThrownBy(() -> userRelationService.createRelation(user1, user2))
                    .isInstanceOf(RuntimeException.class)
                    .hasMessageContaining(message);
        }
    }

}