package com.paymybuddy.domain.repository;

import com.paymybuddy.domain.model.UserAccountModel;
import com.paymybuddy.domain.model.UserRelationModel;

import java.time.LocalDateTime;

public interface UserRelationRepository {
    UserRelationModel getRelation(UserAccountModel user1, UserAccountModel user2);
    void saveRelation(UserAccountModel user1, UserAccountModel user2, LocalDateTime createdAt);
}
