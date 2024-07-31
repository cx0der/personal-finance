package com.moveableapps.pf.commands;

import com.beust.jcommander.Parameter;
import com.beust.jcommander.Parameters;
import com.moveableapps.pf.entities.AccountType;

@Parameters(commandDescription = "Add account")
public class AddAccountCommandArgs extends CommandArgs {

    @Parameter(description = "Name of account", required = true)
    private String accountName;

    @Parameter(names = {"-c", "--currency"}, description = "ISO Currency symbol")
    private String currency;

    @Parameter(names = {"-d", "--desc"}, description = "Account description")
    private String accountDescription = "";

    @Parameter(names = {"-t", "--type"}, description = "Account type")
    private AccountType accountType;

    public AddAccountCommandArgs() {
        // Default constructor
    }

    public String getAccountName() {
        return accountName;
    }

    public String getCurrency() {
        return currency;
    }

    public String getAccountDescription() {
        return accountDescription;
    }

    public AccountType getAccountType() {
        return accountType;
    }

    // constructor for add account
    public AddAccountCommandArgs(String accountName, String accountDescription, String currency, AccountType accountType) {
        this.accountName = accountName;
        this.accountDescription = accountDescription;
        this.currency = currency;
        this.accountType = accountType;
    }
}
