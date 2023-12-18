package com.paymybuddy.utils;

import com.paymybuddy.model.UserAccount;

import java.util.UUID;

public class UserAccountBuilder {
    private UUID userId = null;
    private String firstName = "Whatever";
    private String lastName = "Whatever";
    private String email = "whatever@email.com";
    private String password = "Whatever";
    private String username = "Whatever";

    public UserAccountBuilder(UUID userId, String firstName, String lastName, String email, String password, String username) {
        this.userId = userId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.username = username;
    }

    public UserAccountBuilder() {
    }

    public UserAccountBuilder withId(UUID id) {
        return new UserAccountBuilder(
                id,
                this.firstName,
                this.lastName,
                this.email,
                this.password,
                this.username
        );
    }

    public UserAccountBuilder withFirstName(String firstName) {
        return new UserAccountBuilder(
                this.userId,
                firstName,
                this.lastName,
                this.email,
                this.password,
                this.username
        );
    }

    public UserAccountBuilder withLastName(String lastName) {
        return new UserAccountBuilder(
                this.userId,
                this.firstName,
                lastName,
                this.email,
                this.password,
                this.username
        );
    }

    public UserAccountBuilder withEmail(String email) {
        return new UserAccountBuilder(
                this.userId,
                this.firstName,
                this.lastName,
                email,
                this.password,
                this.username
        );
    }

    public UserAccountBuilder withPassword(String password) {
        return new UserAccountBuilder(
                this.userId,
                this.firstName,
                this.lastName,
                this.email,
                password,
                this.username
        );
    }

    public UserAccountBuilder withUsername(String username) {
        return new UserAccountBuilder(
                this.userId,
                this.firstName,
                this.lastName,
                this.email,
                this.password,
                username
        );
    }

    public UserAccount build() {
        return new UserAccount(
                this.userId,
                this.firstName,
                this.lastName,
                this.email,
                this.password,
                this.username
        );
    }
}
