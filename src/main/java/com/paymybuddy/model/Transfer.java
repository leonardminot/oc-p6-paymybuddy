package com.paymybuddy.model;

import jakarta.persistence.*;
import lombok.*;

@Entity(name = "Transfer")
@Table(name = "transfer")
@Getter
@ToString
@EqualsAndHashCode
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Transfer {
    @EmbeddedId
    private TransferId transferId;

    @ManyToOne
    @MapsId("fromUserId")
    @JoinColumn(
            name = "from_user_id",
            insertable = false,
            updatable = false
    )
    private UserAccount fromUser;

    @ManyToOne
    @MapsId("toUserId")
    @JoinColumn(
            name = "to_user_id",
            insertable = false,
            updatable = false
    )
    private UserAccount toUser;

    @OneToOne(
            cascade = CascadeType.ALL
    )
    @JoinColumn(
            name = "transaction_id",
            insertable = false,
            updatable = false
    )
    private Transaction transaction;

    public Transfer(UserAccount fromUser, UserAccount toUser, Transaction transaction) {
        this.transferId = new TransferId(fromUser.getUserId(), toUser.getUserId(), transaction.getTransactionID());
        this.fromUser = fromUser;
        this.toUser = toUser;
        this.transaction = transaction;
    }
}
