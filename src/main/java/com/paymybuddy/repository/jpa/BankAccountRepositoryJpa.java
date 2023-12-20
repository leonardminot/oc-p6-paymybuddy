package com.paymybuddy.repository.jpa;

import com.paymybuddy.model.BankAccount;
import com.paymybuddy.model.UserAccount;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface BankAccountRepositoryJpa extends CrudRepository<BankAccount, UUID> {
    Optional<BankAccount> findByIban(String iban);


    @Query("SELECT ba FROM BankAccount ba WHERE ba.userAccount = ?1")
    List<BankAccount> findBankAccountsByUserAccountEquals(UserAccount userAccount);
}
