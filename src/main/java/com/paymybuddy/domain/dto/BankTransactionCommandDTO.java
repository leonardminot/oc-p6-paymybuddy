package com.paymybuddy.domain.dto;

import com.paymybuddy.domain.model.BankAccountModel;

import java.time.LocalDateTime;

public record BankTransactionCommandDTO(
        BankAccountModel bankAccount,
        double amount,
        String currency,
        LocalDateTime date
) {
}
