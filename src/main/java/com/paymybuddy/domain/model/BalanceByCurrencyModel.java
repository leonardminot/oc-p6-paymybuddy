package com.paymybuddy.domain.model;

import java.util.Objects;
import java.util.UUID;

public record BalanceByCurrencyModel(
        UUID id,
        UserAccountModel userAccount,
        double balance,
        String currency
) {
    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        BalanceByCurrencyModel that = (BalanceByCurrencyModel) object;
        return Objects.equals(id, that.id) && Objects.equals(userAccount, that.userAccount) && Objects.equals(currency, that.currency);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, userAccount, currency);
    }
}
