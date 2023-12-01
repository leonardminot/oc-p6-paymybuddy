package com.paymybuddy.application.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode
@Entity(name = "BalanceByCurrency")
@Table(name = "balance_by_currency")
public class BalanceByCurrency {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "balance_id")
    private UUID balanceID;
    @Column(
            name = "balance",
            nullable = false
    )
    private Double balance;
    @Column(
            name = "currency",
            nullable = false
    )
    private String currency;

    @ManyToOne
    @JoinColumn(
            name = "user_id",
            referencedColumnName = "user_id"
    )
    private UserAccount userAccount;

    public BalanceByCurrency(Double balance, String currency, UserAccount userAccount) {
        this.balance = balance;
        this.currency = currency;
        this.userAccount = userAccount;
    }

    @Override
    public String toString() {
        return "BalanceByCurrency{" +
                "balanceID=" + balanceID +
                ", balance=" + balance +
                ", currency='" + currency + '\'' +
                ", userAccount=" + userAccount +
                '}';
    }
}
