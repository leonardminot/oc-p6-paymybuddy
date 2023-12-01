package com.paymybuddy.utils;

import com.paymybuddy.coremodel.model.UserAccountModel;
import com.paymybuddy.coremodel.model.UserRelationModel;
import com.paymybuddy.coremodel.repository.UserRelationRepository;

import java.time.LocalDateTime;

public class StubUserRelationRepository implements UserRelationRepository {
    UserRelationModel relation;
    @Override
    public UserRelationModel getRelation(UserAccountModel user1, UserAccountModel user2) {
        return relation;
    }

    @Override
    public void saveRelation(UserAccountModel user1, UserAccountModel user2, LocalDateTime createdAt) {
        relation = new UserRelationModel(user1, user2, createdAt);
    }


}
