package com.moveableapps.pf.commands;

import com.beust.jcommander.IParametersValidator;
import com.beust.jcommander.Parameter;
import com.beust.jcommander.ParameterException;
import com.beust.jcommander.Parameters;
import com.moveableapps.pf.entities.AccountType;

import java.util.Map;

@Parameters(parametersValidators = AccountCommandArgs.class)
public class AccountCommandArgs implements IParametersValidator {

    @Parameter(names = {"-a", "--add"}, description = "Name of account")
    String accountName;

    @Parameter(names = {"-l", "--list"}, description = "List all accounts")
    boolean listAccounts;

    @Parameter(names = {"-c", "--currency"}, description = "ISO Currency symbol")
    String currency;

    @Parameter(names = {"-d", "--desc"}, description = "Account description")
    String accountDescription = "";

    @Parameter(names = {"-t", "--type"}, description = "Account type")
    AccountType accountType;

    public AccountCommandArgs() {
        // Default constructor
    }

    // constructor for add account
    public AccountCommandArgs(String accountName, String accountDescription, String currency, AccountType accountType) {
        this.accountName = accountName;
        this.accountDescription = accountDescription;
        this.currency = currency;
        this.accountType = accountType;
    }

    // constructor for list account
    public AccountCommandArgs(boolean listAccounts) {
        this.listAccounts = listAccounts;
    }

    @Override
    public void validate(Map<String, Object> params) throws ParameterException {
        // Account command has list mode, add mode
        if (params.get("--list") == null && params.get("--add") == null) {
            throw new ParameterException("No option for account is specified");
        }
        if (Boolean.TRUE.equals(params.get("--list")) && params.get("--add") != null) {
            throw new ParameterException("List and add cannot be specified together");
        }

        if (params.get("--add") != null) {
            // Add account mode
            // type, currency cannot be null
            if (params.get("--type") == null || params.get("--currency") == null) {
                throw new ParameterException("Account creation needs type and currency along with name");
            }
        }
    }
}
