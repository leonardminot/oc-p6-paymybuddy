package com.paymybuddy.repository.definition;

import com.paymybuddy.model.Relation;
import com.paymybuddy.model.UserAccount;

import java.time.LocalDateTime;
import java.util.List;

public interface UserRelationRepository {
    Relation getRelation(UserAccount user1, UserAccount user2);
    void saveRelation(UserAccount user1, UserAccount user2, LocalDateTime createdAt);

    List<UserAccount> getAllRelationsForUser(UserAccount user);
    List<Relation> getAllRelations();
}
