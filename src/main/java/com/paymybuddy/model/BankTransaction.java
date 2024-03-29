package com.paymybuddy.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@NoArgsConstructor
@Getter
@EqualsAndHashCode
@Entity(name = "BankTransaction")
@Table(name = "bank_transaction")
public class BankTransaction {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "bank_transaction_id")
    private UUID bankTransactionID;

    @Column(
            nullable = false,
            name = "amount"
    )
    private Double amount;


    @Column(
            name = "currency",
            nullable = false
    )
    @Enumerated(EnumType.STRING)
    private Currency currency;

    @Column(
            name = "date",
            nullable = false
    )
    private LocalDateTime date;

    @ManyToOne
    @JoinColumn(
            name = "bank_account_id",
            nullable = false,
            referencedColumnName = "bank_account_id"
    )
    private BankAccount bankAccount;

    public BankTransaction(UUID bankTransactionID, BankAccount bankAccount, Double amount, Currency currency, LocalDateTime date) {
        this.bankTransactionID = bankTransactionID;
        this.amount = amount;
        this.currency = currency;
        this.date = date;
        this.bankAccount = bankAccount;
    }

    public BankTransaction(Double amount, Currency currency, LocalDateTime date, BankAccount bankAccount) {
        this.amount = amount;
        this.currency = currency;
        this.date = date;
        this.bankAccount = bankAccount;
    }
}
