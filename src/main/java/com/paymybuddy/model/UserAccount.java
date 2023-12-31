package com.paymybuddy.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;
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

    @OneToMany(
            fetch = FetchType.EAGER,
            mappedBy = "userAccount",
            orphanRemoval = true,
            cascade = {CascadeType.PERSIST, CascadeType.REMOVE}
    )
    private List<BalanceByCurrency> balanceByCurrencyList = new ArrayList<>();

    public UserAccount(String firstName, String lastName, String email, String password, String username) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.username = username;
    }

    public UserAccount() {

    }

    public void addBalanceByCurrency(BalanceByCurrency balanceByCurrency) {
        if (!balanceByCurrencyList.contains(balanceByCurrency)) {
            balanceByCurrencyList.add(balanceByCurrency);
            balanceByCurrency.setUserAccount(this);
        }
    }

    public void removeBalanceByCurrency(BalanceByCurrency balanceByCurrency) {
        if (this.balanceByCurrencyList.contains(balanceByCurrency)) {
            this.balanceByCurrencyList.remove(balanceByCurrency);
            balanceByCurrency.setUserAccount(null);
        }
    }
}
