package com.paymybuddy.domain.model;

import java.time.LocalDateTime;
import java.util.UUID;

public record TransactionModel(
        UUID transactionId,
        String description,
        double amount,
        String currency,
        LocalDateTime transactionDate
) {
}
