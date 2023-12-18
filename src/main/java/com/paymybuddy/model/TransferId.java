package com.paymybuddy.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.*;

import java.io.Serializable;
import java.util.UUID;

@Embeddable
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
public class TransferId implements Serializable {
    @Column(name = "from_user_id")
    private UUID fromUserId;

    @Column(name = "to_user_id")
    private UUID toUserId;

    @Column(name = "transaction_id")
    private UUID transactionID;
}
