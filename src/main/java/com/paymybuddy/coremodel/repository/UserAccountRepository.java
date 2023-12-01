package com.paymybuddy.coremodel.repository;

import com.paymybuddy.application.model.UserAccount;
import com.paymybuddy.coremodel.model.UserAccountModel;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserAccountRepository {
    void save(UserAccountModel userAccount);
    boolean isEmailExists(String email);
    boolean isUserNameExists(String userName);
    UserAccountModel get(UserAccountModel userAccount);
    List<UserAccountModel> getAllUsers();
}
