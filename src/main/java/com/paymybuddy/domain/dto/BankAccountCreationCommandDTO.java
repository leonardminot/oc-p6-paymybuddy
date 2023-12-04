package com.paymybuddy.domain.dto;

import com.paymybuddy.domain.model.UserAccountModel;

public record BankAccountCreationCommandDTO(
        UserAccountModel userAccount,
        String iban,
        String country
) { }
