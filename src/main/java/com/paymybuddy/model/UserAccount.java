package com.paymybuddy.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Getter
@Setter
@EqualsAndHashCode
@ToString
@Entity(name = "UserAccount")
@Table(
        name = "user_account",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = "email"),
                @UniqueConstraint(columnNames = "username")
        }
)
public class UserAccount {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "user_id")
    private UUID userId;
    @Column(
            name = "firstname",
            nullable = false
    )
    private String firstName;
    @Column(
            name = "lastname",
            nullable = false
    )
    private String lastName;
    @Column(
            name = "email",
            nullable = false
    )
    private String email;
    @Column(
            name = "password",
            nullable = false
    )
    private String password;
    @Column(
            name = "username",
            nullable = false
    )
    private String username;

    public UserAccount(String firstName, String lastName, String email, String password, String username) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.username = username;
    }

    public UserAccount() {

    }
}
