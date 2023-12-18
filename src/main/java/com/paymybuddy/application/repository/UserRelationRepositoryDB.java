package com.paymybuddy.application.repository;

import com.paymybuddy.application.model.Relation;
import com.paymybuddy.application.model.UserAccount;
import com.paymybuddy.application.repository.jpa.RelationRepositoryJpa;
import com.paymybuddy.application.repository.definition.UserRelationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

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
}
