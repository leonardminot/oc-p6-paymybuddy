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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BalanceByCurrencyModel that = (BalanceByCurrencyModel) o;
        return Objects.equals(id, that.id) && Objects.equals(userAccount, that.userAccount) && Objects.equals(currency, that.currency);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, userAccount, currency);
    }
}
