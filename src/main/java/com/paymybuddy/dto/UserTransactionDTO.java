package com.paymybuddy.dto;

import com.paymybuddy.model.Currency;
import com.paymybuddy.model.UserAccount;

import java.time.LocalDateTime;

public record UserTransactionDTO(
        UserAccount from,
        UserAccount to,
        String description,
        Double amount,
        Currency currency,
        LocalDateTime date
) {
}
