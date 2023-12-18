package com.paymybuddy.domain.dto;

import com.paymybuddy.application.model.UserAccount;

public record BankAccountCreationCommandDTO(
        UserAccount userAccount,
        String iban,
        String country
) { }
