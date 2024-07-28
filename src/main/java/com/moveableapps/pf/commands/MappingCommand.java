package com.moveableapps.pf.commands;

import com.moveableapps.pf.core.BookKeeper;
import com.moveableapps.pf.entities.Account;
import com.moveableapps.pf.entities.AutoMapping;

import java.io.PrintStream;
import java.util.Optional;

public class MappingCommand implements Command {
    public static final String COMMAND_NAME = "map";

    @Override
    public int execute(CommandArgs commandArgs, BookKeeper bookKeeper, PrintStream out, PrintStream err) {
        MappingCommandArgs args = (MappingCommandArgs) commandArgs;
        if (args.listMappings) {
            // List mode
            out.println("Txn Description\tMapped Account");
            bookKeeper.getAllAutoMappings().forEach((key, value) -> {
                String formatted = String.format("%s\t%s\n", key, value.accountId());
                out.print(formatted);
            });
        } else if (args.addMapping) {
            // Add mode
            try {
                addMapping(bookKeeper, args);
            } catch (RuntimeException e) {
                err.println(e.getMessage());
                return 1;
            }
        }
        return 0;
    }

    private void addMapping(BookKeeper bookKeeper, MappingCommandArgs args) {
        Optional<Account> targetAccount = bookKeeper.getAccountByName(args.targetAccount);
        if (targetAccount.isEmpty()) {
            throw new RuntimeException("Cannot find account by the name: " + args.targetAccount);
        }
        if (targetAccount.get().id().isPresent()) {
            AutoMapping mapping = new AutoMapping(args.txnDescription, targetAccount.get().id().getAsLong());
            bookKeeper.addAutoMapping(mapping);
        } else {
            throw new RuntimeException("Cannot find account by the name: " + args.targetAccount);
        }
    }
}
