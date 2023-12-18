package com.paymybuddy.domain.service;

import com.paymybuddy.application.model.UserAccount;
import com.paymybuddy.domain.dto.UserRequestCommandDTO;
import com.paymybuddy.application.repository.definition.UserAccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.UUID;
import java.util.function.Supplier;

@Service
public class UserAccountService {
    private final UserAccountRepository userAccountRepository;

    @Autowired
    public UserAccountService(UserAccountRepository userAccountRepository) {
        this.userAccountRepository = userAccountRepository;
    }

    public UserAccount createUserAccount(UserRequestCommandDTO userRequestCommandDTO) {
        throwIsUserInputIsEmpty(userRequestCommandDTO);
        throwIfEmailAlreadyExists(userRequestCommandDTO);
        throwIfUserNameAlreadyExists(userRequestCommandDTO);

        UserAccount newUserAccount =  new UserAccount(
                UUID.fromString("00000000-0000-0000-0000-000000000000"),
                userRequestCommandDTO.firstName(),
                userRequestCommandDTO.lastName(),
                userRequestCommandDTO.email(),
                userRequestCommandDTO.password(),
                userRequestCommandDTO.username()
        );

        userAccountRepository.save(newUserAccount);

        return newUserAccount;
    }

    private void throwIfUserNameAlreadyExists(UserRequestCommandDTO userRequestCommandDTO) {
        if (userAccountRepository.isUserNameExists(userRequestCommandDTO.username())) {
            throw new RuntimeException("UserName already exists.");
        }
    }

    private void throwIfEmailAlreadyExists(UserRequestCommandDTO userRequestCommandDTO) {
        if (userAccountRepository.isEmailExists(userRequestCommandDTO.email())) {
            throw new RuntimeException("Email already exists.");
        }
    }

    private void throwIsUserInputIsEmpty(UserRequestCommandDTO userRequestCommandDTO) {
        Map<String, Supplier<String>> fields = Map.of(
                "firstName", userRequestCommandDTO::firstName,
                "lastName", userRequestCommandDTO::lastName,
                "email", userRequestCommandDTO::email,
                "password", userRequestCommandDTO::password,
                "username", userRequestCommandDTO::username);

        fields.forEach((fieldName, supplier) -> {
            String value = supplier.get();
            if (value.isEmpty()){
                throw new RuntimeException(fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1).toLowerCase() + " shouldn't be empty.");
            }
        });
    }
}
