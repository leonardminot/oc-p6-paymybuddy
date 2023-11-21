package com.paymybuddy.model;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
@Entity(name = "Relation")
@Table(name = "relation")
public class Relation {
    @EmbeddedId
    private RelationId relationId;

    @ManyToOne
    @MapsId("userId1")
    @JoinColumn(name = "user_id_1")
    private UserAccount user1;

    @ManyToOne
    @MapsId("userId2")
    @JoinColumn(name = "user_id_2")
    private UserAccount user2;
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    public Relation(UserAccount user1, UserAccount user2, LocalDateTime createdAt) {
        this.relationId = new RelationId(user1.getUserId(), user2.getUserId());
        this.user1 = user1;
        this.user2 = user2;
        this.createdAt = createdAt;
    }
}
