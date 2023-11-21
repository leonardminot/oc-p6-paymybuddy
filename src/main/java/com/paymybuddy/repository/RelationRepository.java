package com.paymybuddy.repository;

import com.paymybuddy.model.Relation;
import com.paymybuddy.model.RelationId;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RelationRepository extends CrudRepository<Relation, RelationId> {
}
