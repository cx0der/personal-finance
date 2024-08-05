package com.moveableapps.pf.utils;

import com.moveableapps.pf.commands.LoadCommandArgs;
import com.moveableapps.pf.entities.Account;
import com.moveableapps.pf.entities.AccountType;

public class TestUtils {

    public static Account getIncomeAccount() {
        return new Account("Income:Salary", "Salary account", "INR", AccountType.INCOME);
    }

    public static Account getExpenseAccount() {
        return new Account("Expense:Groceries", "Groceries", "INR", AccountType.EXPENSE);
    }

    public static LoadCommandArgs getLoadArgsWithDefaults(String inputFile, String account, int dateField,
                                                          int amountField, int descriptionField) {
        return new LoadCommandArgs(inputFile, account, dateField, amountField, descriptionField);
    }
}
