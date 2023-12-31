package com.paymybuddy.repository;

import com.paymybuddy.model.UserAccount;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface UserAccountRepository extends CrudRepository<UserAccount, UUID> {
}
