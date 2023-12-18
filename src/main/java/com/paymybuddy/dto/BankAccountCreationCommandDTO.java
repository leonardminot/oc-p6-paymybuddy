package com.paymybuddy.dto;

import com.paymybuddy.model.UserAccount;

public record BankAccountCreationCommandDTO(
        UserAccount userAccount,
        String iban,
        String country
) { }
