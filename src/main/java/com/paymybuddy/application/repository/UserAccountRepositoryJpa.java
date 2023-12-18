package com.paymybuddy.application.repository;

import com.paymybuddy.application.model.UserAccount;
import com.paymybuddy.domain.repository.UserAccountRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface UserAccountRepositoryJpa extends CrudRepository<UserAccount, UUID>, UserAccountRepository {
    UserAccount findByEmail(String email);
}
