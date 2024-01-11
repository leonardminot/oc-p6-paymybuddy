package com.paymybuddy.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.*;

import java.io.Serializable;
import java.util.UUID;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@Embeddable
public class RelationId implements Serializable {
    @Column(name = "user_id_1")
    private UUID userId1;
    @Column(name = "user_id_2")
    private UUID userId2;
}
