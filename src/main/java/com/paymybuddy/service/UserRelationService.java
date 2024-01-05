package com.paymybuddy.service;

import com.paymybuddy.Exception.UserException;
import com.paymybuddy.model.UserAccount;
import com.paymybuddy.repository.definition.UserRelationRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserRelationService {
    private final UserRelationRepository userRelationRepository;
    private final DateProvider dateProvider;

    @Autowired
    public UserRelationService(UserRelationRepository userRelationRepository, DateProvider dateProvider) {
        this.userRelationRepository = userRelationRepository;
        this.dateProvider = dateProvider;
    }

    @Transactional
    public void createRelation(UserAccount user1, UserAccount user2) {
        validateInput(user1, user2);
        userRelationRepository.saveRelation(getFirstUser(user1, user2), getSecondUser(user1, user2), dateProvider.getNow());
    }

    public List<UserAccount> getRelationsFor(UserAccount principalUser) {
        return userRelationRepository.getAllRelationsForUser(principalUser);
    }

    private UserAccount getFirstUser(UserAccount user1, UserAccount user2) {
        return (user1.getUserId().compareTo(user2.getUserId()) > 0 ) ? user2 : user1;
    }

    private UserAccount getSecondUser(UserAccount user1, UserAccount user2) {
        return (user1.getUserId().compareTo(user2.getUserId()) < 0 ) ? user2 : user1;
    }

    private void validateInput(UserAccount user1, UserAccount user2) {
        throwIfAnUserIsEmpty(user1, user2);
        throwIfUserIsTheSame(user1, user2);
    }

    private void throwIfUserIsTheSame(UserAccount user1, UserAccount user2) {
        if (user1 == user2)
            throw new UserException("Users must be different");
    }

    private void throwIfAnUserIsEmpty(UserAccount user1, UserAccount user2) {
        if (user1 == null || user2 == null)
            throw new UserException("Users should not be empty");
    }
}
