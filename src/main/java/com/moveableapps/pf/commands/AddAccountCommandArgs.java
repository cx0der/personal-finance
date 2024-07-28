package com.moveableapps.pf.commands;

import com.beust.jcommander.IParametersValidator;
import com.beust.jcommander.Parameter;
import com.beust.jcommander.ParameterException;
import com.beust.jcommander.Parameters;
import com.moveableapps.pf.entities.AccountType;

import java.util.Map;

@Parameters(parametersValidators = AddAccountCommandArgs.class)
public class AddAccountCommandArgs extends CommandArgs implements IParametersValidator {

    @Parameter(names = {"-a", "--add"}, description = "Name of account")
    String accountName;

    @Parameter(names = {"-c", "--currency"}, description = "ISO Currency symbol")
    String currency;

    @Parameter(names = {"-d", "--desc"}, description = "Account description")
    String accountDescription = "";

    @Parameter(names = {"-t", "--type"}, description = "Account type")
    AccountType accountType;

    public AddAccountCommandArgs() {
        // Default constructor
    }

    // constructor for add account
    public AddAccountCommandArgs(String accountName, String accountDescription, String currency, AccountType accountType) {
        this.accountName = accountName;
        this.accountDescription = accountDescription;
        this.currency = currency;
        this.accountType = accountType;
    }

    @Override
    public void validate(Map<String, Object> params) throws ParameterException {
        if (params.get("--add") != null) {
            // Add account mode
            // type, currency cannot be null
            if (params.get("--type") == null || params.get("--currency") == null) {
                throw new ParameterException("Account creation needs type and currency along with name");
            }
        }
    }
}
