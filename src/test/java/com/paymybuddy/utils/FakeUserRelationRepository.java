package com.paymybuddy.utils;

import com.paymybuddy.application.model.Relation;
import com.paymybuddy.application.model.UserAccount;
import com.paymybuddy.application.repository.definition.UserRelationRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class FakeUserRelationRepository implements UserRelationRepository {
    List<Relation> relations = new ArrayList<>();
    @Override
    public Relation getRelation(UserAccount user1, UserAccount user2) {
        UserAccount userWithSmallestId = user1.getUserId().compareTo(user2.getUserId()) > 0 ? user2 : user1;
        UserAccount userWithBiggestId = user1.getUserId().compareTo(user2.getUserId()) < 0 ? user2 : user1;
        return relations.stream()
                .filter(rel -> rel.getUser1().equals(userWithSmallestId) && rel.getUser2().equals(userWithBiggestId))
                .findAny()
                .orElse(null);
    }

    @Override
    public void saveRelation(UserAccount user1, UserAccount user2, LocalDateTime createdAt) {
        relations.add(new Relation(user1, user2, createdAt));
    }


}
