package com.paymybuddy.model;

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
    @Enumerated(EnumType.STRING)
    private Currency currency;

    @ManyToOne
    @JoinColumn(
            name = "user_id",
            referencedColumnName = "user_id"
    )
    private UserAccount userAccount;

    public BalanceByCurrency(UUID balanceID, UserAccount userAccount, Double balance, Currency currency) {
        this.balanceID = balanceID;
        this.balance = balance;
        this.currency = currency;
        this.userAccount = userAccount;
    }

    public BalanceByCurrency(Double balance, Currency currency, UserAccount userAccount) {
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
