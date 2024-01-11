package com.paymybuddy.service;

import com.paymybuddy.Exception.EmailException;
import com.paymybuddy.Exception.EmptyFieldException;
import com.paymybuddy.Exception.UsernameException;
import com.paymybuddy.model.UserAccount;
import com.paymybuddy.dto.UserRequestCommandDTO;
import com.paymybuddy.repository.definition.UserAccountRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Supplier;

@Service
@Slf4j
public class UserAccountService {
    private final UserAccountRepository userAccountRepository;

    @Autowired
    public UserAccountService(UserAccountRepository userAccountRepository) {
        this.userAccountRepository = userAccountRepository;
    }

    @Transactional
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

    public Optional<UserAccount> getUserWithEmail(String mail) {
            return userAccountRepository.getByEmail(mail);
    }

    private void throwIfUserNameAlreadyExists(UserRequestCommandDTO userRequestCommandDTO) {
        if (userAccountRepository.isUserNameExists(userRequestCommandDTO.username())) {
            throw new UsernameException("UserName already exists.");
        }
    }

    private void throwIfEmailAlreadyExists(UserRequestCommandDTO userRequestCommandDTO) {
        if (userAccountRepository.isEmailExists(userRequestCommandDTO.email())) {
            throw new EmailException("Email already exists.");
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
                throw new EmptyFieldException(fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1).toLowerCase() + " shouldn't be empty.");
            }
        });
    }

    public List<UserAccount> getAllUsers() {
        return userAccountRepository.getAllUsers();
    }
}
