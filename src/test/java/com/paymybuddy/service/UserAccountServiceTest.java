package com.paymybuddy.service;

import com.paymybuddy.Exception.EmailException;
import com.paymybuddy.Exception.EmptyFieldException;
import com.paymybuddy.Exception.UsernameException;
import com.paymybuddy.model.Relation;
import com.paymybuddy.model.UserAccount;
import com.paymybuddy.dto.UserRequestCommandDTO;
import com.paymybuddy.utils.Fixture;
import com.paymybuddy.utils.UserAccountBuilder;
import org.junit.jupiter.api.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

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
                fixture.thenTheUserShouldBeAndSaved(new UserAccount(UUID.fromString("00000000-0000-0000-0000-000000000000"),"Léo", "Minot", "leo@email.com", "123", "LeoM"));
            }

            @Test
            void itShouldThrowWhenUserNameIsEmpty() {
                // Given
                // When
                fixture.whenRequestForCreateUserThatThrow(new UserRequestCommandDTO("", "leo@email.com", "123", "Léo", "Minot"));

                // Then
                fixture.thenItShouldThrowAnException(new EmptyFieldException("Username shouldn't be empty."));
            }

            @Test
            void itShouldThrowWhenFirstNameIsEmpty() {
                // Given
                // When
                fixture.whenRequestForCreateUserThatThrow(new UserRequestCommandDTO("LeoM", "leo@email.com", "123", "", "Minot"));

                // Then
                fixture.thenItShouldThrowAnException(new EmptyFieldException("Firstname shouldn't be empty."));
            }

            @Test
            void itShouldThrowWhenLastNameIsEmpty() {
                // Given
                // When
                fixture.whenRequestForCreateUserThatThrow(new UserRequestCommandDTO("LeoM", "leo@email.com", "123", "Léo", ""));

                // Then
                fixture.thenItShouldThrowAnException(new EmptyFieldException("Lastname shouldn't be empty."));
            }

            @Test
            void itShouldThrowWhenPasswordIsEmpty() {
                // Given
                // When
                fixture.whenRequestForCreateUserThatThrow(new UserRequestCommandDTO("LeoM", "leo@email.com", "", "Léo", "Minot"));

                // Then
                fixture.thenItShouldThrowAnException(new EmptyFieldException("Password shouldn't be empty."));
            }

            @Test
            void itShouldThrowWhenEmailIsEmpty() {
                // Given
                // When
                fixture.whenRequestForCreateUserThatThrow(new UserRequestCommandDTO("LeoM", "", "123", "Léo", "Minot"));

                // Then
                fixture.thenItShouldThrowAnException(new EmptyFieldException("Email shouldn't be empty."));
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
                UserAccount userInDB = new UserAccountBuilder().withEmail("leo@email.com").build();
                fixture.givenUserInDatabase(userInDB);
                // When
                fixture.whenRequestForCreateUserThatThrow(new UserRequestCommandDTO("LeoM", "leo@email.com", "123", "Léo", "Minot"));
                // Then
                fixture.thenItShouldThrowAnException(new EmailException("Email already exists."));
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
                UserAccount userInDB = new UserAccountBuilder().withUsername("LeoM").build();
                fixture.givenUserInDatabase(userInDB);
                // When
                fixture.whenRequestForCreateUserThatThrow(new UserRequestCommandDTO("LeoM", "leo@email.com", "123", "Léo", "Minot"));
                // Then
                fixture.thenItShouldThrowAnException(new UsernameException("UserName already exists."));
            }
        }

    }

    @Nested
    @DisplayName("Feature: get a user account")
    class GetUserAccount {

        @Nested
        @DisplayName("Rule: return a user account with email")
        class GetUserAccountWithEmail {
            private Fixture fixture;
            @BeforeEach
            void setUp() {
                fixture = new Fixture();
            }

            @Test
            void itShouldReturnAUserAccount() {
                // Given
                UserAccount existingUser = new UserAccountBuilder().withFirstName("Léo").withEmail("leo@gmail.com").build();

                fixture.givenUserInDatabase(existingUser);

                // When
                fixture.whenRequestAUserInDatabaseWithEmail("leo@gmail.com");

                // Then
                fixture.thenReturnedUserShouldBe(Optional.of(existingUser));

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
                UserAccount leo = new UserAccountBuilder().withId(UUID.fromString("1124d9e8-6266-4bcf-8035-37a02ba75c69")).withFirstName("Léo").withUsername("LeoM").build();
                UserAccount victor = new UserAccountBuilder().withId(UUID.fromString("2124d9e8-6266-4bcf-8035-37a02ba75c69")).withFirstName("Victor").withUsername("VictorM").build();

                fixture.givenNowIs(LocalDateTime.of(2013, 12, 1, 15, 42, 0, 0));
                // When
                fixture.whenRequestACreationOfARelationBetween(leo, victor);
                // Then
                fixture.thenARelationHasToBeCreateAndEqualTo(new Relation(leo, victor, LocalDateTime.of(2013, 12, 1, 15, 42, 0, 0)));
            }

            @Test
            void itShouldThrowIfUser1IsEmpty() {
                // Given
                UserAccount user = new UserAccountBuilder().build();
                // When
                // Then
                fixture.whenRequestACreationOfARelationBetweenThenThrow(null, user, "Users should not be empty");
            }

            @Test
            void itShouldThrowIfUser2IsEmpty() {
                // Given
                UserAccount user = new UserAccountBuilder().build();
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
                UserAccount user = new UserAccountBuilder().build();
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
                UserAccount userWithSmallestID = new UserAccountBuilder()
                        .withId(UUID.fromString("1124d9e8-6266-4bcf-8035-37a02ba75c69"))
                        .withFirstName("Léo")
                        .withLastName("Minot")
                        .build();
//
                UserAccount userWithHighestId = new UserAccountBuilder()
                        .withId(UUID.fromString("2124d9e8-6266-4bcf-8035-37a02ba75c69"))
                        .withFirstName("Victor")
                        .withLastName("Minot")
                        .build();

                LocalDateTime now = LocalDateTime.of(2013, 12, 1, 15, 42, 0, 0);
                fixture.givenNowIs(now);
                // When
                fixture.whenRequestACreationOfARelationBetween(userWithHighestId, userWithSmallestID); // Victor has a UUID superior in the alphabetical order. At the end, Leo should be first
                // Then
                fixture.thenARelationHasToBeCreateAndEqualTo(new Relation(userWithSmallestID, userWithHighestId, now));
            }
        }
    }

    @Nested
    @DisplayName("Feature: it should return all relations of a User")
    class ReturnRelations {

        @Nested
        @DisplayName("Rule: all relations must be returned")
        class AllRelationMustBeReturned {
            private Fixture fixture;
            @BeforeEach
            void setUp() {
                fixture = new Fixture();
            }

            @Test
            void itShouldReturnAllRelations() {
                // Given
                LocalDateTime now = LocalDateTime.of(2013, 12, 1, 15, 42, 0, 0);
                fixture.givenNowIs(now);

                UserAccount principalUser = new UserAccountBuilder().withId(UUID.fromString("1124d9e8-6266-4bcf-8035-37a02ba75c69"))
                        .withFirstName("Léo")
                        .withLastName("MINOT")
                        .build();

                UserAccount connectedUserWithSmallerId = new UserAccountBuilder().withId(UUID.fromString("1024d9e8-6266-4bcf-8035-37a02ba75c69"))
                        .withFirstName("Victor")
                        .withLastName("MINOT")
                        .build();

                UserAccount connectedUserHigherId = new UserAccountBuilder().withId(UUID.fromString("9024d9e8-6266-4bcf-8035-37a02ba75c69"))
                        .withFirstName("John")
                        .withLastName("TRAVOLTA")
                        .build();

                fixture.givenAConnectionExistsBetween(principalUser, connectedUserHigherId);
                fixture.givenAConnectionExistsBetween(principalUser, connectedUserWithSmallerId);

                // When
                fixture.whenRequestConnectedUserFor(principalUser);

                // Then
                fixture.thenConnectedUserListShouldBe(List.of(connectedUserWithSmallerId, connectedUserHigherId));

            }
        }
    }

}