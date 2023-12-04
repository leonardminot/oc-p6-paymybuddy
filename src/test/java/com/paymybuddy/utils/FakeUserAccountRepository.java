package com.paymybuddy.utils;

import com.paymybuddy.domain.model.UserAccountModel;
import com.paymybuddy.domain.repository.UserAccountRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class FakeUserAccountRepository implements UserAccountRepository {

    private final List<UserAccountModel> users = new ArrayList<>();

    @Override
    public void save(UserAccountModel userAccount) {
       users.add(userAccount);
    }

    @Override
    public boolean isEmailExists(String email) {
        return users.stream().anyMatch(user -> user.email().equals(email));
    }

    @Override
    public boolean isUserNameExists(String userName) {
        return users.stream().anyMatch(user -> user.username().equals(userName));
    }

    @Override
    public Optional<UserAccountModel> get(UserAccountModel userAccount) {
        return users.stream().filter(user -> user.equals(userAccount)).findAny();
    }

    @Override
    public List<UserAccountModel> getAllUsers() {
        return users;
    }
}
