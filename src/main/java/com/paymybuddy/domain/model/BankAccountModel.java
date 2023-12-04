package com.paymybuddy.domain.model;

import java.util.UUID;

public record BankAccountModel(
        UUID bankAccountId,
        UserAccountModel userAccount,
        String IBAN,
        String country
) {
}
