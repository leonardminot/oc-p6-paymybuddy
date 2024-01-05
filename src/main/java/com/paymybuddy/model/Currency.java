package com.paymybuddy.model;

import lombok.Getter;

@Getter
public enum Currency {
    EUR("EUR"),
    USD("USD"),
    GBP("GBP"),
    CAD("CAD");

    private final String displayValue;

    Currency(String displayValue) {
        this.displayValue = displayValue;
    }

}
