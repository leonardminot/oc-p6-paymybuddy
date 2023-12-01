package com.paymybuddy.application.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity(name = "Transaction")
@Table(name = "transaction")
@Getter
@Setter
@NoArgsConstructor
@ToString
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
    private String currency;

    @Column(
            name = "transaction_date",
            nullable = false
    )
    private LocalDateTime transactionDate;

    public Transaction(String description, Double amount, String currency, LocalDateTime transactionDate) {
        this.description = description;
        this.amount = amount;
        this.currency = currency;
        this.transactionDate = transactionDate;
    }
}
