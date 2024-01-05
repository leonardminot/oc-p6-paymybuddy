package com.paymybuddy.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode
@Entity(name = "Transaction")
@Table(name = "transaction")
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "transaction_id")
    private UUID transactionID;
    @Column(
            name = "description",
            nullable = false
    )
    private String description;

    @Column(
            name = "amount",
            nullable = false
    )
    private Double amount;

    @Column(
            name = "currency",
            nullable = false
    )
    @Enumerated(EnumType.STRING)
    private Currency currency;

    @Column(
            name = "transaction_date",
            nullable = false
    )
    private LocalDateTime transactionDate;

    public Transaction(UUID transactionID, String description, Double amount, Currency currency, LocalDateTime transactionDate) {
        this.transactionID = transactionID;
        this.description = description;
        this.amount = amount;
        this.currency = currency;
        this.transactionDate = transactionDate;
    }

    public Transaction(String description, Double amount, Currency currency, LocalDateTime transactionDate) {
        this.description = description;
        this.amount = amount;
        this.currency = currency;
        this.transactionDate = transactionDate;
    }
}


