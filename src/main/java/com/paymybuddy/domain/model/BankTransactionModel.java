package com.paymybuddy.domain.model;

import java.time.LocalDateTime;
import java.util.UUID;

public record BankTransactionModel(
        UUID id,
        BankAccountModel bankAccount,
        double amount,
        String currency,
        LocalDateTime date
) {
}
