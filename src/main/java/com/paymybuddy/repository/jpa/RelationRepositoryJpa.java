package com.paymybuddy.repository.jpa;

import com.paymybuddy.model.Relation;
import com.paymybuddy.model.RelationId;
import com.paymybuddy.model.UserAccount;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RelationRepositoryJpa extends CrudRepository<Relation, RelationId> {
    Relation getRelationByUser1AndUser2(UserAccount user1, UserAccount user2);
}
