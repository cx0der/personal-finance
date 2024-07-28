package com.moveableapps.pf.commands;

import com.moveableapps.pf.core.BookKeeper;
import com.moveableapps.pf.entities.Account;
import com.moveableapps.pf.entities.AutoMapping;

import java.io.PrintStream;
import java.util.Optional;

public class AddMappingCommand implements Command {
    public static final String COMMAND_NAME = "add-mapping";

    @Override
    public int execute(CommandArgs commandArgs, BookKeeper bookKeeper, PrintStream out, PrintStream err) {
        AddMappingCommandArgs args = (AddMappingCommandArgs) commandArgs;
        try {
            addMapping(bookKeeper, args);
        } catch (RuntimeException e) {
            err.println(e.getMessage());
            return 1;
        }
        return 0;
    }

    private void addMapping(BookKeeper bookKeeper, AddMappingCommandArgs args) {
        Optional<Account> targetAccount = bookKeeper.getAccountByName(args.getAccountName());
        if (targetAccount.isEmpty()) {
            throw new RuntimeException("Cannot find account by the name: " + args.getAccountName());
        }
        if (targetAccount.get().id().isPresent()) {
            AutoMapping mapping = new AutoMapping(args.getDescription(), targetAccount.get().id().getAsLong());
            bookKeeper.addAutoMapping(mapping);
        } else {
            throw new RuntimeException("Cannot find account by the name: " + args.getAccountName());
        }
    }
}
