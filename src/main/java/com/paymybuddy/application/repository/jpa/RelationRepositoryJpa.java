package com.paymybuddy.application.repository.jpa;

import com.paymybuddy.application.model.Relation;
import com.paymybuddy.application.model.RelationId;
import com.paymybuddy.application.model.UserAccount;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RelationRepositoryJpa extends CrudRepository<Relation, RelationId> {
    Relation getRelationByUser1AndUser2(UserAccount user1, UserAccount user2);
}
