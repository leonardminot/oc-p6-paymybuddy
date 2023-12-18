package com.paymybuddy.utils;

import com.paymybuddy.application.model.UserAccount;
import com.paymybuddy.application.repository.definition.UserAccountRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class FakeUserAccountRepository implements UserAccountRepository {

    private final List<UserAccount> users = new ArrayList<>();

    @Override
    public void save(UserAccount userAccount) {
       users.add(userAccount);
    }

    @Override
    public boolean isEmailExists(String email) {
        return users.stream().anyMatch(user -> user.getEmail().equals(email));
    }

    @Override
    public boolean isUserNameExists(String userName) {
        return users.stream().anyMatch(user -> user.getUsername().equals(userName));
    }

    @Override
    public Optional<UserAccount> get(UserAccount userAccount) {
        return users.stream().filter(user -> user.equals(userAccount)).findAny();
    }

    @Override
    public List<UserAccount> getAllUsers() {
        return users;
    }
}
