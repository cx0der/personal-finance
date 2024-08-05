package com.moveableapps.pf.commands;

import com.beust.jcommander.Parameter;
import com.beust.jcommander.Parameters;

@Parameters(commandDescription = "Add mapping")
public class AddMappingCommandArgs implements CommandArgs {
    @Parameter(names = {"-d", "--desc"}, description = "Transaction description", required = true)
    private String txnDescription;

    @Parameter(names = {"-t", "--target"}, description = "Target account name", required = true)
    private String targetAccount;

    public AddMappingCommandArgs() {
        // Default Constructor
    }

    public AddMappingCommandArgs(String description, String accountName) {
        this.txnDescription = description;
        this.targetAccount = accountName;
    }

    public String getDescription() {
        return this.txnDescription;
    }

    public String getAccountName() {
        return this.targetAccount;
    }
}
