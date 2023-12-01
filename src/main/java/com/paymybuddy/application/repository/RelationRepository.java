package com.paymybuddy.application.repository;

import com.paymybuddy.application.model.Relation;
import com.paymybuddy.application.model.RelationId;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RelationRepository extends CrudRepository<Relation, RelationId> {
}
