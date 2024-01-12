package com.moveableapps.pf.commands;

import com.moveableapps.pf.core.BookKeeper;
import com.moveableapps.pf.data.Repository;
import com.moveableapps.pf.entities.Account;

import java.io.PrintStream;

public class AccountCommand extends Command {
    private final Repository repository;
    private final AccountCommandArgs args;

    public AccountCommand(Repository repository, AccountCommandArgs accountCommandArgs, PrintStream out, PrintStream err) {
        super(out, err);
        this.repository = repository;
        this.args = accountCommandArgs;
    }

    @Override
    public int execute() throws RuntimeException {
        BookKeeper bookKeeper = new BookKeeper(repository);
        if (args.listAccounts) {
            out.println("Id\tName\tDescription\tType");
            bookKeeper.getAllAccounts().stream()
                    .map(account -> String.format("%d\t%s\t%s\t%s", account.id().getAsLong(), account.name(), account.description(), account.type()))
                    .forEach(out::println);
        } else {
            try {
                bookKeeper.addAccount(new Account(args.accountName, args.accountDescription, args.currency, args.accountType));
            } catch (RuntimeException e) {
                err.println(e.getMessage());
                return 1;
            }
        }
        return 0;
    }
}
