package com.moveableapps.pf.commands;

import com.moveableapps.pf.entities.AccountType;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;

@Command(name = "account", description = "Manage accounts")
public class AccountCommand implements Runnable {

    private final Logger logger = LogManager.getLogger(AccountCommand.class);

    @Option(names = {"-a", "--add"}, paramLabel = "ACCOUNT NAME", description = "Name of account", required = true)
    String accountName;

    @Option(names = {"-d", "--desc"}, paramLabel = "ACCOUNT DESCRIPTION", description = "Account description (default: '')", defaultValue = "")
    String accountDescription;

    @Option(names = {"-t", "--type"}, paramLabel = "ACCOUNT TYPE", description = "Valid values: EQUITY, INCOME, ASSET, LIABILITY, EXPENSE")
    AccountType accountType;

    @Override
    public void run() {
        logger.traceEntry("run()");
        logger.info("accountName: " + accountName);
        logger.info("accountDescription: " + accountDescription);
        logger.info("accountType: " + accountType);
        logger.traceExit();
    }
}
