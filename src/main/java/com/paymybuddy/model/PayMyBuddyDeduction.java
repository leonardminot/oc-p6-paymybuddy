package com.paymybuddy.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity(name = "PayMyBuddyDeduction")
@Table(
        name = "pay_my_buddy_deduction"
)
@Getter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@ToString
public class PayMyBuddyDeduction {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "deduction_id")
    private UUID deductionID;
    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "transaction_id", referencedColumnName = "transaction_id")
    private Transaction transaction;
    @Column(
            name = "amount",
            nullable = false
    )
    private Double amount;

    public PayMyBuddyDeduction(Transaction transaction, Double amount) {
        this.transaction = transaction;
        this.amount = amount;
    }
}
