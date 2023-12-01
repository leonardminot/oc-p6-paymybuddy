package com.paymybuddy.coremodel.repository;

import com.paymybuddy.application.model.UserAccount;
import com.paymybuddy.coremodel.model.UserAccountModel;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserAccountRepository {
    void save(UserAccount userAccount);
    boolean isEmailExists(String email);
    boolean isUserNameExists(String userName);
    UserAccount get(UserAccount userAccount);
    List<UserAccountModel> getAllUsers();
}
