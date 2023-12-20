package com.paymybuddy.repository;

import com.paymybuddy.model.UserAccount;
import com.paymybuddy.repository.jpa.UserAccountRepositoryJpa;
import com.paymybuddy.repository.definition.UserAccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;

@Repository
public class UserAccountRepositoryDB implements UserAccountRepository {

    private final UserAccountRepositoryJpa userAccountRepositoryJpa;

    @Autowired
    public UserAccountRepositoryDB(UserAccountRepositoryJpa userAccountRepositoryJpa) {
        this.userAccountRepositoryJpa = userAccountRepositoryJpa;
    }

    @Override
    public void save(UserAccount userAccount) {
        userAccountRepositoryJpa.save(userAccount);
    }

    @Override
    public boolean isEmailExists(String email) {
        return userAccountRepositoryJpa.findByEmail(email).isPresent();
    }

    @Override
    public boolean isUserNameExists(String userName) {
        return userAccountRepositoryJpa.findByUsername(userName).isPresent();
    }

    @Override
    public Optional<UserAccount> get(UserAccount userAccount) {
        return userAccountRepositoryJpa.findById(userAccount.getUserId());
    }

    @Override
    public List<UserAccount> getAllUsers() {
        Iterable<UserAccount> users = userAccountRepositoryJpa.findAll();
        return StreamSupport.stream(users.spliterator(), false).toList();
    }

    @Override
    public Optional<UserAccount> getByEmail(String email) {
        return userAccountRepositoryJpa.findByEmail(email);
    }
}
