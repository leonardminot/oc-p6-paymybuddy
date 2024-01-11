package com.paymybuddy.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode
@Entity(name = "BankAccount")
@ToString
@Table(
        name = "bank_account",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = "iban")
        }
)
public class BankAccount {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "bank_account_id")
    private UUID bankAccountId;

    @Column(
            name = "iban",
            nullable = false
    )
    private String iban;

    @Column(
            name = "country",
            nullable = false
    )
    private String country;

    @ManyToOne
    @JoinColumn(
            name = "user_id",
            referencedColumnName = "user_id"
    )
    private UserAccount userAccount;

    @OneToMany(
            mappedBy = "bankAccount",
            orphanRemoval = true,
            cascade = CascadeType.ALL,
            fetch = FetchType.EAGER
    )
    private List<BankTransaction> bankTransactions = new ArrayList<>();

    public BankAccount(UUID bankAccountId, UserAccount userAccount, String iban, String country) {
        this.bankAccountId = bankAccountId;
        this.iban = iban;
        this.country = country;
        this.userAccount = userAccount;
    }

    public void addTransaction(BankTransaction bankTransaction) {
        if (!bankTransactions.contains(bankTransaction)) {
            bankTransactions.add(bankTransaction);
            bankTransaction.setBankAccount(this);
        }
    }

    public void removeTransaction(BankTransaction bankTransaction) {
        if (bankTransactions.contains(bankTransaction)) {
            bankTransactions.remove(bankTransaction);
            bankTransaction.setBankAccount(null);
        }
    }

    public BankAccount(String iban, String country, UserAccount userAccount) {
        this.iban = iban;
        this.country = country;
        this.userAccount = userAccount;
    }



}
