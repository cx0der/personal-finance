package com.moveableapps.pf.commands;

import com.moveableapps.pf.core.BookKeeper;
import com.moveableapps.pf.data.Repository;
import com.moveableapps.pf.entities.Account;
import com.moveableapps.pf.entities.AutoMapping;

import java.io.PrintStream;
import java.util.Optional;

public class MappingCommand extends Command {

    private final Repository repository;
    private final MappingCommandArgs args;

    public MappingCommand(Repository repository, MappingCommandArgs mappingCommandArgs, PrintStream out, PrintStream err) {
        super(out, err);
        this.repository = repository;
        this.args = mappingCommandArgs;
    }

    @Override
    public int execute() {
        BookKeeper bookKeeper = new BookKeeper(repository);
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
                addMapping(bookKeeper);
            } catch (RuntimeException e) {
                err.println(e.getMessage());
                return 1;
            }
        }
        return 0;
    }

    private void addMapping(BookKeeper bookKeeper) {
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
