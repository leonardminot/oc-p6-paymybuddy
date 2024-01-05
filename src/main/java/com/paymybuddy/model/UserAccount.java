package com.paymybuddy.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Getter
@Setter
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

    @Column(
            name = "role",
            nullable = false
    )
    private String role;

    @OneToMany(
            fetch = FetchType.EAGER,
            mappedBy = "userAccount",
            orphanRemoval = true,
            cascade = {CascadeType.PERSIST, CascadeType.REMOVE}
    )
    private List<BalanceByCurrency> balanceByCurrencyList = new ArrayList<>();

    @OneToMany(
            fetch = FetchType.EAGER,
            mappedBy = "userAccount",
            orphanRemoval = true,
            cascade = {CascadeType.PERSIST, CascadeType.REMOVE}
    )
    private List<BankAccount> bankAccounts = new ArrayList<>();

    public UserAccount(UUID userId, String firstName, String lastName, String email, String password, String username) {
        this.userId = userId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.username = username;
        this.role = "USER";
    }

    public UserAccount(String firstName, String lastName, String email, String password, String username, String role) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.username = username;
        this.role = role;
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

    public void addBankAccount(BankAccount bankAccount) {
        if (!this.bankAccounts.contains(bankAccount)) {
            bankAccounts.add(bankAccount);
            bankAccount.setUserAccount(this);
        }
    }

    public void removeBankAccount(BankAccount bankAccount) {
        if (this.bankAccounts.contains(bankAccount)) {
            this.bankAccounts.remove(bankAccount);
            bankAccount.setUserAccount(null);
        }
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        UserAccount that = (UserAccount) obj;
        return Objects.equals(userId, that.userId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId);
    }
}
