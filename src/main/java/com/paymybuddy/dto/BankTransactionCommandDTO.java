package com.paymybuddy.dto;

import com.paymybuddy.model.BankAccount;

public record BankTransactionCommandDTO(
        BankAccount bankAccount,
        double amount,
        String currency
) {
}
