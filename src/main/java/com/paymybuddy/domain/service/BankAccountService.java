package com.paymybuddy.domain.service;

import com.paymybuddy.application.model.BankAccount;
import com.paymybuddy.domain.dto.BankAccountCreationCommandDTO;
import com.paymybuddy.application.repository.definition.BankAccountRepository;
import com.paymybuddy.application.repository.definition.UserAccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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


    public void create(BankAccountCreationCommandDTO bankAccountCreationCommandDTO) {
        throwIfAFieldIsEmpty(bankAccountCreationCommandDTO);
        throwIfUserIsUnknown(bankAccountCreationCommandDTO);
        throwIfIBANAlreadyExists(bankAccountCreationCommandDTO);

        this.bankAccountRepository.save(new BankAccount(
                UUID.fromString("00000000-0000-0000-0000-000000000000"),
                bankAccountCreationCommandDTO.userAccount(),
                bankAccountCreationCommandDTO.iban(),
                bankAccountCreationCommandDTO.country()
        ));
    }

    private void throwIfAFieldIsEmpty(BankAccountCreationCommandDTO bankAccountCreationCommandDTO) {
        throwIfUserAccountIsEmpty(bankAccountCreationCommandDTO);
        throwIfIBANIsEmpty(bankAccountCreationCommandDTO);
        throwIfCountryIsEmpty(bankAccountCreationCommandDTO);
    }

    private void throwIfUserAccountIsEmpty(BankAccountCreationCommandDTO bankAccountCreationCommandDTO) {
        if (bankAccountCreationCommandDTO.userAccount() == null)
            throw new RuntimeException("User must be not null");
    }

    private void throwIfIBANIsEmpty(BankAccountCreationCommandDTO bankAccountCreationCommandDTO) {
        if (bankAccountCreationCommandDTO.iban() == null)
            throw new RuntimeException("IBAN must be not null");
    }

    private void throwIfCountryIsEmpty(BankAccountCreationCommandDTO bankAccountCreationCommandDTO) {
        if (bankAccountCreationCommandDTO.country() == null)
            throw new RuntimeException("Country must be not null");
    }

    private void throwIfUserIsUnknown(BankAccountCreationCommandDTO bankAccountCreationCommandDTO) {
        if (this.userAccountRepository.get(bankAccountCreationCommandDTO.userAccount()).isEmpty()) {
            throw new RuntimeException("User is unknown");
        }
    }

    private void throwIfIBANAlreadyExists(BankAccountCreationCommandDTO bankAccountCreationCommandDTO) {
        if (bankAccountRepository.isIBANExists(bankAccountCreationCommandDTO.iban()))
            throw new RuntimeException("IBAN already exists");
    }
}
