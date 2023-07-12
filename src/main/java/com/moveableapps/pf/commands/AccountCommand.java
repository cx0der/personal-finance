package com.moveableapps.pf.commands;

import com.moveableapps.pf.Main;
import com.moveableapps.pf.core.BookKeeper;
import com.moveableapps.pf.data.DBRepository;
import com.moveableapps.pf.data.MemoryRepository;
import com.moveableapps.pf.data.Repository;
import com.moveableapps.pf.entities.Account;
import com.moveableapps.pf.entities.AccountType;
import com.moveableapps.pf.utils.Utils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;
import picocli.CommandLine.ParentCommand;

import java.util.concurrent.Callable;

@Command(name = "account", description = "Manage accounts")
public class AccountCommand implements Callable<Integer> {

    private final Logger logger = LogManager.getLogger(AccountCommand.class);

    @Option(names = {"-a", "--add"}, paramLabel = "ACCOUNT NAME", description = "Name of account", required = true)
    String accountName;

    @Option(names = {"-c", "--currency"}, paramLabel = "CURRENCY", description = "ISO Currency symbol", required = true)
    String currency;

    @Option(names = {"-d", "--desc"}, paramLabel = "ACCOUNT DESCRIPTION", description = "Account description (default: '')", defaultValue = "")
    String accountDescription;

    @Option(names = {"-t", "--type"}, paramLabel = "ACCOUNT TYPE", description = "Valid values: EQUITY, INCOME, ASSET, LIABILITY, EXPENSE")
    AccountType accountType;

    @ParentCommand
    Main main;

    @Override
    public Integer call() throws RuntimeException {
        logger.traceEntry("call()");
        Repository repository;
        if (main.getRepo().equalsIgnoreCase("memory")) {
            repository = new MemoryRepository();
        } else {
            repository = new DBRepository(Utils.getDatabasePath(logger));
        }
        BookKeeper bookKeeper = new BookKeeper(repository, logger);
        bookKeeper.addAccount(new Account(accountName, accountDescription, currency, accountType));
        logger.traceExit();
        return 0;
    }
}
