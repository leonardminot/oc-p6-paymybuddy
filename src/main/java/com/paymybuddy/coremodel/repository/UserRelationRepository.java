package com.paymybuddy.coremodel.repository;

import com.paymybuddy.coremodel.model.UserAccountModel;
import com.paymybuddy.coremodel.model.UserRelationModel;

import java.time.LocalDateTime;

public interface UserRelationRepository {
    UserRelationModel getRelation(UserAccountModel user1, UserAccountModel user2);
    void saveRelation(UserAccountModel user1, UserAccountModel user2, LocalDateTime createdAt);
}
