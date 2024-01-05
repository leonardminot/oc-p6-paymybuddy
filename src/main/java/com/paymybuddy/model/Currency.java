package com.paymybuddy.model;

public enum Currency {
    EUR("EUR"),
    USD("USD"),
    GBP("GBP"),
    CAD("CAD");

    private final String displayValue;

    Currency(String displayValue) {
        this.displayValue = displayValue;
    }

    public String getDisplayValue() {
        return displayValue;
    }
}
