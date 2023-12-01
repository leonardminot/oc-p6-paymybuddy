package com.paymybuddy.coremodel.service;

import com.paymybuddy.coremodel.model.UserAccountModel;
import com.paymybuddy.coremodel.repository.UserRelationRepository;

public class UserRelationService {
    private final UserRelationRepository userRelationRepository;
    private final DateProvider dateProvider;

    public UserRelationService(UserRelationRepository userRelationRepository, DateProvider dateProvider) {
        this.userRelationRepository = userRelationRepository;
        this.dateProvider = dateProvider;
    }

    public void createRelation(UserAccountModel user1, UserAccountModel user2) {
        validateInput(user1, user2);
        userRelationRepository.saveRelation(getFirstUser(user1, user2), getSecondUser(user1, user2), dateProvider.getNow());
    }

    private UserAccountModel getFirstUser(UserAccountModel user1, UserAccountModel user2) {
        return (user1.id().compareTo(user2.id()) > 0 ) ? user2 : user1;
    }

    private UserAccountModel getSecondUser(UserAccountModel user1, UserAccountModel user2) {
        return (user1.id().compareTo(user2.id()) < 0 ) ? user2 : user1;
    }

    private void validateInput(UserAccountModel user1, UserAccountModel user2) {
        throwIfAnUserIsEmpty(user1, user2);
        throwIfUserIsTheSame(user1, user2);
    }

    private void throwIfUserIsTheSame(UserAccountModel user1, UserAccountModel user2) {
        if (user1 == user2)
            throw new RuntimeException("Users must be different");
    }

    private void throwIfAnUserIsEmpty(UserAccountModel user1, UserAccountModel user2) {
        if (user1 == null || user2 == null)
            throw new RuntimeException("Users should not be empty");
    }
}
