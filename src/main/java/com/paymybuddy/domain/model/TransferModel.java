package com.paymybuddy.domain.model;

public record TransferModel(
        UserAccountModel fromUser,
        UserAccountModel toUser,
        TransactionModel transaction
) {
}
