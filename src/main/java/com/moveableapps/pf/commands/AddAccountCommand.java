package com.moveableapps.pf.commands;

import com.moveableapps.pf.core.BookKeeper;
import com.moveableapps.pf.data.Repository;
import com.moveableapps.pf.entities.Account;

import java.awt.print.Book;
import java.io.PrintStream;

public class AddAccountCommand implements Command {
    @Override
    public int execute(CommandArgs commandArgs, BookKeeper bookKeeper, PrintStream out, PrintStream err) throws RuntimeException {
        AddAccountCommandArgs args = (AddAccountCommandArgs) commandArgs;
        try {
            bookKeeper.addAccount(new Account(args.accountName, args.accountDescription, args.currency, args.accountType));
        } catch (RuntimeException e) {
            err.println(e.getMessage());
            return 1;
        }
        return 0;
    }
}
