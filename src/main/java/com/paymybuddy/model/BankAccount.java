package com.paymybuddy.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@ToString
@EqualsAndHashCode
@Entity(name = "BankAccount")
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

    public BankAccount(String iban, String country, UserAccount userAccount) {
        this.iban = iban;
        this.country = country;
        this.userAccount = userAccount;
    }
}
