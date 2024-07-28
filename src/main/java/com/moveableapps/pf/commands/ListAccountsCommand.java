package com.moveableapps.pf.commands;

import com.moveableapps.pf.core.BookKeeper;
import com.moveableapps.pf.entities.Account;

import java.io.PrintStream;
import java.util.List;

public class ListAccountsCommand implements Command {
    public static final String COMMAND_NAME = "list-accounts";

    @Override
    public int execute(CommandArgs commandArgs, BookKeeper bookKeeper, PrintStream out, PrintStream err) {
        ListAccountsArgs args = (ListAccountsArgs) commandArgs;

        List<Account> allAccounts = bookKeeper.getAllAccounts();

        if (args.getPattern() != null && !args.getPattern().isEmpty()) {
            printAccounts(allAccounts.stream().filter(account -> account.name().matches(args.getPattern())).toList(), out);
        } else {
            printAccounts(allAccounts, out);
        }
        return 0;
    }

    private void printAccounts(List<Account> accounts, PrintStream out) {
        out.println("Id\tName\tDescription\tType");
        //noinspection OptionalGetWithoutIsPresent
        accounts.stream().map(account -> String.format("%d\t%s\t%s\t%s", account.id().getAsLong(), account.name(),
                        account.description(), account.type()))
                .forEach(out::println);
    }
}
