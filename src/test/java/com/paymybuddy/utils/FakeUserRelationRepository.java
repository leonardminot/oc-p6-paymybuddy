package com.paymybuddy.utils;

import com.paymybuddy.domain.model.UserAccountModel;
import com.paymybuddy.domain.model.UserRelationModel;
import com.paymybuddy.domain.repository.UserRelationRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class FakeUserRelationRepository implements UserRelationRepository {
    List<UserRelationModel> relations = new ArrayList<>();
    @Override
    public UserRelationModel getRelation(UserAccountModel user1, UserAccountModel user2) {
        UserAccountModel userWithSmallestId = user1.id().compareTo(user2.id()) > 0 ? user2 : user1;
        UserAccountModel userWithBiggestId = user1.id().compareTo(user2.id()) < 0 ? user2 : user1;
        return relations.stream()
                .filter(rel -> rel.user1().equals(userWithSmallestId) && rel.user2().equals(userWithBiggestId))
                .findAny()
                .orElse(null);
    }

    @Override
    public void saveRelation(UserAccountModel user1, UserAccountModel user2, LocalDateTime createdAt) {
        relations.add(new UserRelationModel(user1, user2, createdAt));
    }


}
