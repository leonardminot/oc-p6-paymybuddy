package com.paymybuddy.application.repository;

import com.paymybuddy.application.model.UserAccount;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface UserAccountRepositoryJpa
        extends CrudRepository<UserAccount, UUID>{
}