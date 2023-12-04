package com.paymybuddy.domain.model;

import java.util.UUID;

public record BalanceByCurrencyModel(
        UUID id,
        UserAccountModel userAccount,
        double balance,
        String currency
) {
}
