package com.paymybuddy.application.repository.definition;

import com.paymybuddy.application.model.Relation;
import com.paymybuddy.application.model.UserAccount;

import java.time.LocalDateTime;

public interface UserRelationRepository {
    Relation getRelation(UserAccount user1, UserAccount user2);
    void saveRelation(UserAccount user1, UserAccount user2, LocalDateTime createdAt);
}
