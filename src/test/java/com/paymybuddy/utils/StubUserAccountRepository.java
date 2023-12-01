package com.paymybuddy.utils;

import com.paymybuddy.application.model.UserAccount;
import com.paymybuddy.coremodel.model.UserAccountModel;
import com.paymybuddy.coremodel.repository.UserAccountRepository;

import java.util.List;

public class StubUserAccountRepository implements UserAccountRepository {
    private UserAccount inMemoryUserAccount;
    private boolean isAnExistingEmail = false;

    private boolean isAnExistingUserName = false;

    private List<UserAccountModel> users;
    @Override
    public void save(UserAccount userAccount) {
        inMemoryUserAccount = userAccount;
    }

    @Override
    public boolean isEmailExists(String email) {
        return isAnExistingEmail;
    }

    @Override
    public boolean isUserNameExists(String userName) {
        return isAnExistingUserName;
    }

    @Override
    public UserAccount get(UserAccount userAccount) {
        return inMemoryUserAccount;
    }

    @Override
    public List<UserAccountModel> getAllUsers() {
        return users;
    }

    public void setAnExistingEmail(boolean anExistingEmail) {
        isAnExistingEmail = anExistingEmail;
    }

    public void setAnExistingUserName(boolean anExistingUserName) {
        isAnExistingUserName = anExistingUserName;
    }

    public void setUsers(List<UserAccountModel> users) {
        this.users = users;
    }
}
