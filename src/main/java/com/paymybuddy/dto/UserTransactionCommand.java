package com.paymybuddy.dto;

import com.paymybuddy.model.UserAccount;

public record UserTransactionCommand(
        UserAccount fromUser,
        UserAccount toUser,
        String description,
        String currency,
        double amount
) {
}
