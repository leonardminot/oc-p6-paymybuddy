package com.paymybuddy.domain.repository;

import com.paymybuddy.domain.model.UserAccountModel;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserAccountRepository {
    void save(UserAccountModel userAccount);
    boolean isEmailExists(String email);
    boolean isUserNameExists(String userName);
    Optional<UserAccountModel> get(UserAccountModel userAccount);
    List<UserAccountModel> getAllUsers();
}
