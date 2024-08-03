package com.moveableapps.pf.utils;

import com.moveableapps.pf.entities.Account;
import com.moveableapps.pf.entities.AccountType;

public class TestUtils {

    public static Account getIncomeAccount() {
        return new Account("Income:Salary", "Salary account", "INR", AccountType.INCOME);
    }
}
