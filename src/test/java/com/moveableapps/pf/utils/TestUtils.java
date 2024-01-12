package com.moveableapps.pf.utils;

import com.moveableapps.pf.entities.Account;
import com.moveableapps.pf.entities.AccountType;
import com.moveableapps.pf.entities.AutoMapping;

public class TestUtils {

    public static Account getIncomeAccount() {
        return new Account("Income:Salary", "Salary account", "INR", AccountType.INCOME);
    }

    public static AutoMapping getTestAutoMapping() {
        return new AutoMapping("groceries", 1);
    }
}
