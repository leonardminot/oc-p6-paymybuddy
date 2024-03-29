package com.paymybuddy.service;

import com.paymybuddy.Exception.EmptyFieldException;
import com.paymybuddy.Exception.IBANException;
import com.paymybuddy.Exception.UserException;
import com.paymybuddy.model.BankAccount;
import com.paymybuddy.dto.BankAccountCreationCommandDTO;
import com.paymybuddy.model.UserAccount;
import com.paymybuddy.repository.definition.BankAccountRepository;
import com.paymybuddy.repository.definition.UserAccountRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class BankAccountService {
    private final BankAccountRepository bankAccountRepository;

    private final UserAccountRepository userAccountRepository;

    @Autowired
    public BankAccountService(BankAccountRepository bankAccountRepository, UserAccountRepository userAccountRepository) {
        this.bankAccountRepository = bankAccountRepository;
        this.userAccountRepository = userAccountRepository;
    }


    @Transactional
    public BankAccount create(BankAccountCreationCommandDTO bankAccountCreationCommandDTO) {
        throwIfAFieldIsEmpty(bankAccountCreationCommandDTO);
        throwIfUserIsUnknown(bankAccountCreationCommandDTO);
        throwIfIBANAlreadyExists(bankAccountCreationCommandDTO);

        BankAccount newBankAccount = new BankAccount(
                UUID.fromString("00000000-0000-0000-0000-000000000000"),
                bankAccountCreationCommandDTO.userAccount(),
                bankAccountCreationCommandDTO.iban(),
                bankAccountCreationCommandDTO.country()
        );
        return this.bankAccountRepository.save(newBankAccount);

    }

    public List<BankAccount> getBankAccountsFor(UserAccount user) {
        return bankAccountRepository.fetchAllBankAccountsForUser(user);
    }

    public Optional<BankAccount> getBankAccount(UUID bankAccountId) {
        return bankAccountRepository.getById(bankAccountId);
    }

    private void throwIfAFieldIsEmpty(BankAccountCreationCommandDTO bankAccountCreationCommandDTO) {
        throwIfUserAccountIsEmpty(bankAccountCreationCommandDTO);
        throwIfIBANIsEmpty(bankAccountCreationCommandDTO);
        throwIfCountryIsEmpty(bankAccountCreationCommandDTO);
    }

    private void throwIfUserAccountIsEmpty(BankAccountCreationCommandDTO bankAccountCreationCommandDTO) {
        if (bankAccountCreationCommandDTO.userAccount() == null)
            throw new UserException("User must be not null");
    }

    private void throwIfIBANIsEmpty(BankAccountCreationCommandDTO bankAccountCreationCommandDTO) {
        if (bankAccountCreationCommandDTO.iban() == null || bankAccountCreationCommandDTO.iban().isEmpty())
            throw new EmptyFieldException("IBAN must be not null");
    }

    private void throwIfCountryIsEmpty(BankAccountCreationCommandDTO bankAccountCreationCommandDTO) {
        if (bankAccountCreationCommandDTO.country() == null || bankAccountCreationCommandDTO.country().isEmpty())
            throw new EmptyFieldException("Country must be not null");
    }

    private void throwIfUserIsUnknown(BankAccountCreationCommandDTO bankAccountCreationCommandDTO) {
        if (this.userAccountRepository.get(bankAccountCreationCommandDTO.userAccount()).isEmpty()) {
            throw new UserException("User is unknown");
        }
    }

    private void throwIfIBANAlreadyExists(BankAccountCreationCommandDTO bankAccountCreationCommandDTO) {
        if (bankAccountRepository.isIBANExists(bankAccountCreationCommandDTO.iban()))
            throw new IBANException("IBAN already exists");
    }
}
