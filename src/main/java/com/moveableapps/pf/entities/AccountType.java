package com.moveableapps.pf.entities;

public enum AccountType {
    EQUITY("Equity"),
    INCOME("Income"),
    ASSET("Asset"),
    LIABILITY("Liability"),
    EXPENSE("Expense");

    private final String value;

    AccountType(String value) {
        this.value = value;
    }

    public String value() {
        return value;
    }
}
