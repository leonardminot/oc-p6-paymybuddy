package com.paymybuddy.service;

import com.paymybuddy.dto.UserRequestCommandDTO;
import com.paymybuddy.model.UserAccount;
import com.paymybuddy.repository.UserAccountRepositoryJpa;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.function.Supplier;

@Service
public class UserAccountService {
    private final UserAccountRepositoryJpa userAccountRepositoryJpa;

    @Autowired
    public UserAccountService(UserAccountRepositoryJpa userAccountRepositoryJpa) {
        this.userAccountRepositoryJpa = userAccountRepositoryJpa;
    }

    public UserAccount createUserAccount(UserRequestCommandDTO userRequestCommandDTO) {
        throwIsUserInputIsEmpty(userRequestCommandDTO);


        UserAccount newUserAccount =  new UserAccount(
                userRequestCommandDTO.firstName(),
                userRequestCommandDTO.lastName(),
                userRequestCommandDTO.email(),
                userRequestCommandDTO.password(),
                userRequestCommandDTO.username()
        );

        userAccountRepositoryJpa.save(newUserAccount);

        return newUserAccount;
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
