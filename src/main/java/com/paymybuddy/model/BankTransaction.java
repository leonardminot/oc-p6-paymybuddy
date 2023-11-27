package com.paymybuddy.model;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@NoArgsConstructor
@Getter
@Setter
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
    private String currency;

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

    public BankTransaction(Double amount, String currency, LocalDateTime date, BankAccount bankAccount) {
        this.amount = amount;
        this.currency = currency;
        this.date = date;
        this.bankAccount = bankAccount;
    }

    public BankTransaction(Double amount, String currency, LocalDateTime date) {
        this.amount = amount;
        this.currency = currency;
        this.date = date;
    }
}
