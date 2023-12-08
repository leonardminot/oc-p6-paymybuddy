package com.paymybuddy.domain.dto;

import com.paymybuddy.domain.model.UserAccountModel;

public record UserTransactionCommand(
        UserAccountModel fromUser,
        UserAccountModel toUser,
        String description,
        String currency,
        double amount
) {
}
