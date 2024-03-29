package com.paymybuddy.utils;

import com.paymybuddy.model.Relation;
import com.paymybuddy.model.UserAccount;
import com.paymybuddy.repository.definition.UserRelationRepository;

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

    @Override
    public List<UserAccount> getAllRelationsForUser(UserAccount user) {
        List<UserAccount> connectedUser = new ArrayList<>();

        for (Relation relation : relations) {
            if (relation.getUser1() == user)
                connectedUser.add(relation.getUser2());
            if (relation.getUser2() == user)
                connectedUser.add(relation.getUser1());
        }
        return connectedUser;
    }

    @Override
    public List<Relation> getAllRelations() {
        return relations;
    }


}
