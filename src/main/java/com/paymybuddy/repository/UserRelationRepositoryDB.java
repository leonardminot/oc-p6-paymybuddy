package com.paymybuddy.repository;

import com.paymybuddy.model.Relation;
import com.paymybuddy.model.UserAccount;
import com.paymybuddy.repository.jpa.RelationRepositoryJpa;
import com.paymybuddy.repository.definition.UserRelationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Repository
public class UserRelationRepositoryDB implements UserRelationRepository {
    private final RelationRepositoryJpa relationRepositoryJpa;

    @Autowired
    public UserRelationRepositoryDB(RelationRepositoryJpa relationRepositoryJpa) {
        this.relationRepositoryJpa = relationRepositoryJpa;
    }

    @Override
    public Relation getRelation(UserAccount user1, UserAccount user2) {
        return relationRepositoryJpa.getRelationByUser1AndUser2(user1, user2);
    }

    @Override
    public void saveRelation(UserAccount user1, UserAccount user2, LocalDateTime createdAt) {
        relationRepositoryJpa.save(new Relation(user1, user2, createdAt));
    }

    @Override
    public List<UserAccount> getAllRelationsForUser(UserAccount user) {
        List<UserAccount> connectedUser = new ArrayList<>();
        Iterable<Relation> allRelations = relationRepositoryJpa.findAll();
        for (Relation relation : allRelations) {
            if (relation.getUser1() == user)
                connectedUser.add(relation.getUser2());
            if (relation.getUser2() == user)
                connectedUser.add(relation.getUser1());
        }
        return connectedUser;
    }
}