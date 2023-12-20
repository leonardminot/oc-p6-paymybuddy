package com.paymybuddy.repository.definition;

import com.paymybuddy.model.UserAccount;

import java.util.List;
import java.util.Optional;


public interface UserAccountRepository {
    void save(UserAccount userAccount);
    boolean isEmailExists(String email);
    boolean isUserNameExists(String userName);
    Optional<UserAccount> get(UserAccount userAccount);
    List<UserAccount> getAllUsers();

    Optional<UserAccount> getByEmail(String email);
}
