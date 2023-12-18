package com.paymybuddy.domain.dto;

import com.paymybuddy.application.model.UserAccount;

public record UserTransactionCommand(
        UserAccount fromUser,
        UserAccount toUser,
        String description,
        String currency,
        double amount
) {
}
