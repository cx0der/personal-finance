package com.moveableapps.pf.commands;

import com.moveableapps.pf.core.BookKeeper;
import com.moveableapps.pf.entities.Account;

import java.io.PrintStream;

public class AddAccountCommand implements Command {
    public static final String COMMAND_NAME = "add-account";

    @Override
    public int execute(CommandArgs commandArgs, BookKeeper bookKeeper, PrintStream out, PrintStream err) throws RuntimeException {
        AddAccountCommandArgs args = (AddAccountCommandArgs) commandArgs;
        try {
            Account account = bookKeeper.addAccount(new Account(args.getAccountName(), args.getAccountDescription(),
                    args.getCurrency(), args.getAccountType()));
            // We just successfully created the account, and it should definitely have an id
            //noinspection OptionalGetWithoutIsPresent
            out.println("Created account: " + args.getAccountName() + " with id: " + account.id().getAsLong());
        } catch (RuntimeException e) {
            err.println(e.getMessage());
            return 1;
        }
        return 0;
    }
}
