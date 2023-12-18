package com.paymybuddy.domain.dto;

import com.paymybuddy.application.model.BankAccount;

public record BankTransactionCommandDTO(
        BankAccount bankAccount,
        double amount,
        String currency
) {
}
